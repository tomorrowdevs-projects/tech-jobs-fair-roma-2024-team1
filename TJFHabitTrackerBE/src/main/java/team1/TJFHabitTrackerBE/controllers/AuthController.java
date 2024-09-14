package team1.TJFHabitTrackerBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserDTO;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserLoginDTO;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserLoginResponseDTO;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserResponseDTO;
import team1.TJFHabitTrackerBE.servicies.AuthService;
import team1.TJFHabitTrackerBE.servicies.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO payload) {
        return new UserLoginResponseDTO(authService.authenticateUtenteAndGenerateToken(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO saveUtenti(@RequestBody @Validated UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        System.out.println(body);
        return new UserResponseDTO(this.userService.saveUser(body).getId());
    }
}