package team1.TJFHabitTrackerBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsResponseDTO;
import team1.TJFHabitTrackerBE.servicies.HabitsService;

import java.util.UUID;

@RestController
@RequestMapping("/habits")
public class HabitsController {


    @Autowired
    private HabitsService habitsService;


    @GetMapping
    public Page<Habits> getHabits(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {

        return this.habitsService.getAllHabits(page, size, sortBy);
    }

    @PostMapping
    public HabitsResponseDTO saveHabits(@RequestBody @Validated HabitsDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        System.out.println(body);
        return new HabitsResponseDTO(this.habitsService.saveHabits(body).getId());

    }

    @DeleteMapping("/{habitsId}")
    public void deleteHabits(@PathVariable UUID habitsId) {
        habitsService.findHabitsByIdAndDelete(habitsId);
    }
}
