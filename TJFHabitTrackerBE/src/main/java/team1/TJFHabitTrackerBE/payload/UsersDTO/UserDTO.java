package team1.TJFHabitTrackerBE.payload.UsersDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


public record UserDTO(
   @NotEmpty(message = "The field id is required")
   String id,
   @NotEmpty(message = "The field email is required")
   String email,
   @NotNull(message = "The field created at is required")
   LocalDateTime createdAt,
   LocalDateTime updatedAt
) {
}
