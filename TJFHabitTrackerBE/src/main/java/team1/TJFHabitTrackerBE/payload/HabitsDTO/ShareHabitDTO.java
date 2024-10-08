package team1.TJFHabitTrackerBE.payload.HabitsDTO;

import jakarta.validation.constraints.Email;

public record ShareHabitDTO(
        @Email
        String email
) {
}
