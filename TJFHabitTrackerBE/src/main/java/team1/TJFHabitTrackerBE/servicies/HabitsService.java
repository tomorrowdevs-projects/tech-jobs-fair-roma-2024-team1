package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.Notifications;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.enums.Frequency;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.exceptions.NotFoundException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.CompleteHabits;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.payload.NotificationsDTO.NotificationsDTO;
import team1.TJFHabitTrackerBE.repositories.HabitsRepository;
import team1.TJFHabitTrackerBE.repositories.NotificationsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HabitsService {
    @Autowired
    private HabitsRepository habitsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationsRepository notificationsRepository;



    public Page<Habits> getAllHabits(int pageNumber, int pageSize, String sortBy, String userId) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return habitsRepository.findByUserId(userId, pageable);
    }

    public Page<Habits> getAllHabitsCompleted(int pageNumber, int pageSize, String sortBy, String userId) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return habitsRepository.findByUserIdAndCompleted(userId, true, pageable);
    }

    public Habits saveHabits(HabitsDTO body, String userId) {

        User found = this.userService.findById(userId);
        Habits habit = new Habits(body.name(), convertStringToFrequency(body.frequency()), body.reminder(), body.createdAt(), body.updatedAt(), body.completed(), found);

        return habitsRepository.save(habit);
    }


    public Habits findById(UUID id) {
        return this.habitsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }


    public void deleteHabits(UUID id) {
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

    public Habits modifyCompleted(UUID id, CompleteHabits payload) {
        Habits found = this.findById(id);

        found.setCompleted(payload.completed());
        found.setUpdatedAt(LocalDateTime.now());

        return habitsRepository.save(found);
    }


    public List<Habits> saveHabitsByFrequency(HabitsDTO body) {
        User found = this.userService.findById(body.user());
        Frequency frequency = convertStringToFrequency(body.frequency());
        List<Habits> createdHabits = new ArrayList<>();

        // Logic to create habits based on frequency
        if (frequency == Frequency.EVERYDAY) {
            for (int i = 0; i < 7; i++) { // Create 7 daily habits
                Habits habit = new Habits(body.name(), frequency, body.reminder(), body.createdAt().plusDays(i), body.updatedAt(), body.completed(), found);
                createdHabits.add(habitsRepository.save(habit));
                createNotificationIfReminder(habit, found);
            }
        } else if (frequency == Frequency.EVERY3DAYS) {
            for (int i = 0; i < 4; i++) { // Create 4 habits every 3 days
                Habits habit = new Habits(body.name(), frequency, body.reminder(), body.createdAt().plusDays(i * 3), body.updatedAt(), body.completed(), found);
                createdHabits.add(habitsRepository.save(habit));
                createNotificationIfReminder(habit, found);
            }
        } else if (frequency == Frequency.ONCEAWEEK) {
            for (int i = 0; i < 12; i++) { // Create 12 weekly habits
                Habits habit = new Habits(body.name(), frequency, body.reminder(), body.createdAt().plusWeeks(i), body.updatedAt(), body.completed(), found);
                createdHabits.add(habitsRepository.save(habit));
                createNotificationIfReminder(habit, found);
            }
        } else if (frequency == Frequency.ONCEAMONTH) {
            for (int i = 0; i < 12; i++) { // Create 12 monthly habits
                Habits habit = new Habits(body.name(), frequency, body.reminder(), body.createdAt().plusMonths(i), body.updatedAt(), body.completed(), found);
                createdHabits.add(habitsRepository.save(habit));
                createNotificationIfReminder(habit, found);
            }
        }

        return createdHabits;
    }

    private void createNotificationIfReminder(Habits habit, User user) {
        if (habit.isReminder()) {
            Notifications notify = new Notifications(user, habit, "Remember your habit: " + habit.getName(), LocalDateTime.now(), null);
            notificationsRepository.save(notify);
        }
    }



}
