package team1.TJFHabitTrackerBE.payload.HabitsDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record HabitsDTO(
        @NotEmpty(message = "The name field is required")
        @Size(min = 2, message = "The name field is min 2 characters")
        String name,
        @NotEmpty(message = "The frequency field is required")
        String frequency,
        boolean reminder,
        UUID user
) {
}
