package team1.TJFHabitTrackerBE.payload.UsersDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public record UserDTO(
    @NotEmpty(message = "Name field is required")
    @Size(min = 2, max= 12, message = "The name must be between 2 and 12 characters")
    String name,
    @NotEmpty(message = "Surname field is required")
    @Size(min = 2, max = 16, message = "The surname must be between 2 and 16 characters")
    String surname,
    @NotEmpty(message = "Email field is required")
    @Email
    String email,
    @NotEmpty(message = "Password field is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    String password
) {
}
