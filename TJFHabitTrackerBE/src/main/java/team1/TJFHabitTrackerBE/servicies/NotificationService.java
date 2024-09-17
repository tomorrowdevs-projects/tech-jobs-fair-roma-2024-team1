package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.entities.User;
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
    private UserService userService;
    @Autowired
    private HabitsService habitsService;



    public Page<Notifications> getAllNotifications(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return notificationsRepository.findAll(pageable);
    }


    public Notifications saveNotifications(NotificationsDTO body) {
        User user = this.userService.findById(body.user());
        Habits habit = this.habitsService.findById(body.habits());


        Notifications notify = new Notifications(user, habit, body.message(), body.scheduledAt(), body.sent_at());

        return notificationsRepository.save(notify);
    }


    public Notifications saveNotificationsDefault(String userId, UUID habitId) {
        User user = this.userService.findById(userId);
        Habits habit = this.habitsService.findById(habitId);


        Notifications notify = new Notifications(user, habit, "Remember your habit:" + habit.getName(), LocalDateTime.now(), null);

        return notificationsRepository.save(notify);
    }

}
