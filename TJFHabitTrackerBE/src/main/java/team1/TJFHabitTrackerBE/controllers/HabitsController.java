package team1.TJFHabitTrackerBE.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team1.TJFHabitTrackerBE.entities.HabitCompletion;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.exceptions.NotFoundException;
import team1.TJFHabitTrackerBE.exceptions.UnauthorizedException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitCompletionResponseDTO;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsResponseDTO;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserDTO;
import team1.TJFHabitTrackerBE.security.JwtTool;
import team1.TJFHabitTrackerBE.servicies.HabitsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/habits")
public class HabitsController {

@Autowired
private JwtTool jwtTool;
    @Autowired
    private HabitsService habitsService;

// get all habits
@GetMapping
public Page<Habits> getHabits(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,

        @RequestHeader("Authorization") String token) {
    String userId = jwtTool.extractIdFromToken(token.replace("Bearer ", ""));

    return this.habitsService.getAllHabits(page, size, sortBy, userId);
}



    // post habits

    @PostMapping
    public List<HabitsResponseDTO> saveHabits(@RequestBody @Validated HabitsDTO body,
                                              BindingResult validationResult,
                                              @AuthenticationPrincipal User user) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors().toString());
        }

        // Salva l'abitudine utilizzando il metodo unificato
        Habits savedHabit = this.habitsService.saveHabits(body, user.getId().toString());

        // Restituisce la risposta come lista di HabitsResponseDTO
        return List.of(new HabitsResponseDTO(savedHabit.getId()));
    }
// delete habit
    @DeleteMapping("/{habitsId}")
    public void deleteHabits(@PathVariable UUID habitsId, @AuthenticationPrincipal User user) {
        Habits habit = habitsService.findById(habitsId);
        if (!habit.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this habit.");
        }

        habitsService.deleteHabits(habitsId);

    }

    // complete habit


    @PatchMapping("/{habitsId}/complete")
    public   ResponseEntity<?>  completeHabit(@PathVariable UUID habitsId, @AuthenticationPrincipal User user) {
        try {
            // Chiama il servizio per completare l'abitudine
            HabitCompletion completion = habitsService.completeHabit(habitsId, user);

            // Restituisce il DTO in caso di successo
            HabitCompletionResponseDTO response = new HabitCompletionResponseDTO(completion.getId(), completion.getCompletedAt());
            return ResponseEntity.ok(response);

        } catch (BadRequestException e) {
            // Restituisce una stringa in caso di errore specifico
            return ResponseEntity.badRequest().body("Errore nella richiesta: " + e.getMessage());

        } catch (Exception e) {
            // Restituisce una stringa con un messaggio di errore generico in caso di errore del server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... abbiamo sbagliato qualcosa...");
        }

    }

    // get completion
    @GetMapping("/completions")
    public Page<HabitCompletion> getCompletions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "completedAt") String sortBy,
            @AuthenticationPrincipal User user) {
        return habitsService.getCompletions(page, size, sortBy, user);
    }

    // update habit

    @PatchMapping("/{habitsId}")
    public Habits updateHabits(
            @PathVariable UUID habitsId,
            @RequestBody @Validated HabitsDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal User user) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors().toString());
        }
        return habitsService.updateHabits(habitsId, body, user);
    }




// share habit
@PostMapping("/{habitsId}/share")
public void shareHabit(
        @PathVariable UUID habitsId,
        @RequestBody List<String> userIds,
        @AuthenticationPrincipal User currentUser) {
    Habits habit = habitsService.findById(habitsId);
    if (!habit.getOwner().getId().equals(currentUser.getId())) {
        throw new NotFoundException("You are not authorized to share this habit.");
    }

    for (String userId : userIds) {
        User user = habitsService.getUserService().findById(userId);
        habit.addUser(user);
    }

    habitsService.findById(habitsId); // Salva automaticamente tramite il repository
}
    @GetMapping("/{habitsId}/shared-with")
    public List<UserDTO> getSharedUsers(
            @PathVariable UUID habitsId,
            @AuthenticationPrincipal User currentUser) {
        Habits habit = habitsService.findById(habitsId);
        if (!habit.getUsers().contains(currentUser)) {
            throw new NotFoundException("You are not authorized to view shared users for this habit.");
        }

        return habit.getUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());
    }
}

