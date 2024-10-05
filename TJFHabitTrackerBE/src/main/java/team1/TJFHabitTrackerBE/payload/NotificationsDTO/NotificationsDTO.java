package team1.TJFHabitTrackerBE.payload.NotificationsDTO;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationsDTO(
        String user,
        UUID habits,
        String message,
        LocalDateTime scheduledAt,
        LocalDateTime sentAt
) {
}
