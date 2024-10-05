package team1.TJFHabitTrackerBE.payload.HabitsDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record HabitCompletionResponseDTO(
        UUID habitId,
        LocalDateTime completeAt
) {
}
