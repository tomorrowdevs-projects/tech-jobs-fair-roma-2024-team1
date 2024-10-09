package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.enums.Frequency;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsDTO;
import team1.TJFHabitTrackerBE.repositories.NotificationsRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {
    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;


    public Page<Notifications> getAllNotifications(String userId, int pageNumber, int pageSize, String sortBy) {
        User user = userService.findById(userId);
        if (pageSize > 20) pageSize = 20; // Limita la dimensione massima della pagina
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return notificationsRepository.findByUserId(userId, pageable);
    }
    public Notifications saveNotificationsDefault(String userId, UUID habitId) {
        User user = userService.findById(userId);
        // Assicurati che tu possa recuperare l'abitudine se necessario
        // Habits habit = habitsService.findById(habitId);
        Habits habit = null; // Sostituisci con il recupero corretto se necessario

        Notifications notify = new Notifications(user, habit, "Ricorda la tua abitudine: " + (habit != null ? habit.getName() : "Unknown"), LocalDateTime.now(), null);
        Notifications savedNotification = notificationsRepository.save(notify);

        sendNotification(savedNotification);

        return savedNotification;
    }
    public Notifications saveNotifications(NotificationsDTO body) {
        User user = userService.findById(body.user());


        Habits habit = null;

        Notifications notify = new Notifications(user, habit, body.message(), body.scheduledAt(), null);
        Notifications savedNotification = notificationsRepository.save(notify);


        if (!notify.getScheduledAt().isAfter(LocalDateTime.now())) {
            sendNotification(savedNotification);
        }

        return savedNotification;
    }


    /**
     * Crea una notifica e invia una notifica tramite WebSocket se il promemoria è attivo.
     */
    public void createNotificationIfReminder(Habits habit, User user) {
        if (habit.isReminder()) {
            Notifications notify = new Notifications(user, habit, "Ricorda la tua abitudine: " + habit.getName(), LocalDateTime.now(), null);
            Notifications savedNotification = notificationsRepository.save(notify);
            sendNotification(savedNotification);
        }
    }
/**Creazione notifica per ogni data frequenza */
private void generateFrequencyDates(Habits habit, Frequency frequency) {
    LocalDateTime now = LocalDateTime.now();

    switch (frequency) {
        case EVERYDAY:
            for (int i = 0; i < 30; i++) { // circa 30 giorni
                habit.getFrequencyDates().add(now.plusDays(i));
                for (User user : habit.getUsers()){
                createNotificationIfReminder(habit, user);
                }
            }
            break;
        case EVERY3DAYS:
            for (int i = 0; i < 10; i++) { // circa 10 occorrenze (30 / 3)
                habit.getFrequencyDates().add(now.plusDays(i * 3));
                for (User user : habit.getUsers()){
                    createNotificationIfReminder(habit, user);
                }
            }
            break;
        case ONCEAWEEK:
            for (int i = 0; i < 4; i++) { // circa 4 settimane in un mese
                habit.getFrequencyDates().add(now.plusWeeks(i));
                for (User user : habit.getUsers()){
                    createNotificationIfReminder(habit, user);
                }
            }
            break;
        case ONCEAMONTH:
            for (int i = 0; i < 12; i++) {
            habit.getFrequencyDates().add(now.withDayOfMonth(1)); // Aggiungi 1 per mese

                for (User user : habit.getUsers()){
                    createNotificationIfReminder(habit, user);
                }
            }
            break;
    }
}
    /**
     * Invia una notifica tramite WebSocket.
     */
    private void sendNotification(Notifications notification) {
        String destination = "/topic/notifications/" + notification.getUser().getId();
        messagingTemplate.convertAndSend(destination, notification);
        // Aggiorna sentAt
        notification.setSentAt(LocalDateTime.now());
        notificationsRepository.save(notification);
    }


    /**
     * Ascolta gli eventi di creazione dell'abitudine e crea notifiche.
     *
     * @param event Evento di creazione dell'abitudine.
     */
    @EventListener
    public void handleHabitCreatedEvent(HabitCreatedEvent event) {
        Habits habit = event.getHabit();
        User user = event.getUser();

        // Crea una notifica se necessario
        if (habit.isReminder()) {
            Notifications notify = new Notifications(user, habit, "Ricorda la tua abitudine: " + habit.getName(), LocalDateTime.now(), null);
            Notifications savedNotification = notificationsRepository.save(notify);
            sendNotification(savedNotification);
        }
    }

}
