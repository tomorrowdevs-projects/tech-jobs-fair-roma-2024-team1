package team1.TJFHabitTrackerBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.TJFHabitTrackerBE.entities.Notifications;

import java.util.UUID;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {
}
