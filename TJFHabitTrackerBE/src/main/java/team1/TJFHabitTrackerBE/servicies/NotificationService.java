package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.repositories.NotificationsRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationsRepository notificationsRepository;



    public Page<Notifications> getAllNotifications(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return notificationsRepository.findAll(pageable);
    }


}
