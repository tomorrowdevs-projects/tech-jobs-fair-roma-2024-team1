package team1.TJFHabitTrackerBE.payload.HabitsDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HabitsDTO(
        @NotEmpty (message = "Il campo name è obbligatorio")
        String name,
        @NotEmpty (message = "Il campo frequency è obbligatorio")
        String frequency,
        boolean reminder,
        boolean completed,
        List<String> users, // Lista di ID utente per abitudini condivise
        @NotEmpty (message = "Il campo category è obbligatorio")
        String category// stringa per categoria
) {
}
