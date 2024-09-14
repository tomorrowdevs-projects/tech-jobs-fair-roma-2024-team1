package team1.TJFHabitTrackerBE.payload;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
