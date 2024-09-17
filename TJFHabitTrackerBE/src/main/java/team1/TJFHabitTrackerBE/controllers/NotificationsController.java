package team1.TJFHabitTrackerBE.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsResponseDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsResponseDTO;
import team1.TJFHabitTrackerBE.servicies.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public Page<Notifications> getNotifications(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String sortBy) {

        return this.notificationService.getAllNotifications(page, size, sortBy);
    }


    @PostMapping
    public NotificationsResponseDTO saveNotifications(@RequestBody @Validated NotificationsDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        System.out.println(body);
        return new NotificationsResponseDTO(this.notificationService.saveNotifications(body).getId());

    }

}
