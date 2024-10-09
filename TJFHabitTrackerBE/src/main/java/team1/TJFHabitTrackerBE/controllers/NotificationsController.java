package team1.TJFHabitTrackerBE.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.exceptions.UnauthorizedException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsResponseDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsResponseDTO;
import team1.TJFHabitTrackerBE.security.JwtTool;
import team1.TJFHabitTrackerBE.servicies.NotificationService;
import team1.TJFHabitTrackerBE.servicies.UserService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @GetMapping
    public Page<Notifications> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @AuthenticationPrincipal User user) {

        return this.notificationService.getAllNotifications(user.getId(), page, size, sortBy);
    }

    @PostMapping
    public NotificationsResponseDTO saveNotifications(
            @RequestBody @Validated NotificationsDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors().toString());
        }
        System.out.println(body);
        Notifications savedNotification = this.notificationService.saveNotifications(body);
        return new NotificationsResponseDTO(savedNotification.getId());
    }


}
