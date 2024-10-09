package team1.TJFHabitTrackerBE.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserDTO;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserResponseDTO;
import team1.TJFHabitTrackerBE.servicies.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/saveUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO saveUser(@RequestBody @Validated UserDTO body, BindingResult validationResult, HttpServletResponse response) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }

        return new UserResponseDTO(this.userService.saveUserAndSetCookie(body, response));
    }
}