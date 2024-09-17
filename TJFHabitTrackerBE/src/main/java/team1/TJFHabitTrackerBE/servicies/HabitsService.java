package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.enums.Frequency;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.exceptions.NotFoundException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.repositories.HabitsRepository;

import java.util.UUID;

@Service
public class HabitsService {
    @Autowired
    private HabitsRepository habitsRepository;
    @Autowired
    private UserService userService;


    public Page<Habits> getAllHabits(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return habitsRepository.findAll(pageable);
    }

    public Habits saveHabits(HabitsDTO body) {
        User found = this.userService.findById(body.user());
        Habits habit = new Habits(body.name(), convertStringToFrequency(body.frequency()), body.reminder(), body.createdAt(), body.updatedAt(), found);

        return habitsRepository.save(habit);
    }


    public Habits findById(UUID id) {
        return this.habitsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findHabitsByIdAndDelete(UUID id) {
        Habits found = this.findById(id);
        this.habitsRepository.delete(found);
    }


//    METODO DI CONVERSIONE DA STRINGA A ENUM (FREQUENCY)
    private static Frequency convertStringToFrequency (String resType){
        if (resType == null) {
            return null;
        }
        try{
            return Frequency.valueOf(resType.toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new BadRequestException("The selected reservation frequency don't exists");
        }
    }
}
