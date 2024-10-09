package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.enums.Frequency;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsDTO;
import team1.TJFHabitTrackerBE.repositories.NotificationsRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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


        return notificationsRepository.save(notify);
    }

    public Notifications saveNotifications(NotificationsDTO body) {
        User user = userService.findById(body.user());

        Habits habit = null;

        Notifications notify = new Notifications(user, habit, body.message(), body.scheduledAt(), null);


        return notificationsRepository.save(notify);
    }

    /**
     * Crea una notifica e invia una notifica tramite WebSocket se il promemoria è attivo.
     */
    public void createNotificationIfReminder(Habits habit, User user) {
        if (habit.isReminder()) {
            Notifications notify = new Notifications(user, habit, "Ricorda la tua abitudine: " + habit.getName(), LocalDateTime.now(), null);
             notificationsRepository.save(notify);

        }
    }








}
