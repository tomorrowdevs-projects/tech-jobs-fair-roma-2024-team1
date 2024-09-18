package team1.TJFHabitTrackerBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsResponseDTO;
import team1.TJFHabitTrackerBE.servicies.HabitsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/habits")
public class HabitsController {


    @Autowired
    private HabitsService habitsService;


    @GetMapping
    public Page<Habits> getHabits(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy, @AuthenticationPrincipal User user) {

        return this.habitsService.getAllHabits(page, size, sortBy);
    }

    @GetMapping("/completed")
    public Page<Habits> getHabitsCompleted(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {

        return this.habitsService.getAllHabitsCompleted(page, size, sortBy);
    }

    @PostMapping
    public HabitsResponseDTO saveHabits(@RequestBody @Validated HabitsDTO body, BindingResult validationResult, @AuthenticationPrincipal User user){
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        System.out.println(body);
        return new HabitsResponseDTO(this.habitsService.saveHabits(body, user.getId()).getId());

    }

    @DeleteMapping("/{habitsId}")
    public void deleteHabits(@PathVariable UUID habitsId) {
        habitsService.findHabitsByIdAndDelete(habitsId);
    }


    @PatchMapping("/{habitsId}")
    public Habits modifyComplete(@PathVariable UUID habitsId, @RequestBody HabitsDTO body) {
        return this.habitsService.modifyCompleted(habitsId, body);

    }

    @PostMapping("/frequencies")
    public List<HabitsResponseDTO> saveHabitsByFrequency(@RequestBody @Validated HabitsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        System.out.println(body);
        List<Habits> savedHabits = this.habitsService.saveHabitsByFrequency(body);
        return savedHabits.stream()
                .map(habit -> new HabitsResponseDTO(habit.getId()))
                .collect(Collectors.toList());
    }




}
