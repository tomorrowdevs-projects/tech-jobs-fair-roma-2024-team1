package team1.TJFHabitTrackerBE.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.TJFHabitTrackerBE.entities.Notifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {
    List<Notifications> findByScheduledAtBeforeAndSentAtIsNull(LocalDateTime time);
    Page<Notifications> findByUserId(String userId, Pageable pageable);
}
