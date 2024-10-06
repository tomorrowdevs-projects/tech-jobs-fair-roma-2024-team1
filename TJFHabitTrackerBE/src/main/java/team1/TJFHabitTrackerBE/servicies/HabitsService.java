package team1.TJFHabitTrackerBE.servicies;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.*;
import team1.TJFHabitTrackerBE.enums.Frequency;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.exceptions.NotFoundException;
import team1.TJFHabitTrackerBE.payload.HabitsDTO.HabitsDTO;
import team1.TJFHabitTrackerBE.repositories.HabitCompletionRepository;
import team1.TJFHabitTrackerBE.repositories.HabitsRepository;
import team1.TJFHabitTrackerBE.repositories.NotificationsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HabitsService {
    @Autowired
    private HabitsRepository habitsRepository;

    @Getter
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HabitCompletionRepository habitCompletionRepository;


    @Autowired
    private ApplicationEventPublisher eventPublisher;

// get all habit
public Page<Habits> getAllHabits(int pageNumber, int pageSize, String sortBy, String userId) {



    if (pageSize > 20) pageSize = 20;
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());

    // Recupera abitudini proprietarie
    Page<Habits> ownedHabits = habitsRepository.findByOwnerIdAndCompleted(userId, false, pageable);

    // Recupera abitudini condivise
    Page<Habits> sharedHabits = habitsRepository.findByUsers_IdAndCompleted(userId, false, pageable);


    List<Habits> combinedList = new ArrayList<>();
    combinedList.addAll(ownedHabits.getContent());
    combinedList.addAll(sharedHabits.getContent());

    return new PageImpl<>(combinedList, pageable, ownedHabits.getTotalElements() + sharedHabits.getTotalElements());
}

// save habit
    public Habits saveHabits(HabitsDTO body, String currentUserId) {
User currentUser = userService.findById(currentUserId);
Category category = categoryService.findByName(body.category());

        Habits habit = new Habits(
                body.name(),
                convertStringToFrequency(body.frequency()),
                body.reminder(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                body.completed(),
                category,
                currentUser
        );
        // Aggiungi utenti condivisi
        for (String userId : body.users()) {
            if (!userId.equals(currentUserId)) { // Evita di aggiungere il creatore due volte
                User user = userService.findById(userId);
                habit.addUser(user);
            }
        }
        Habits savedHabit = habitsRepository.save(habit);


        // Pubblica un evento per notificare la creazione dell'abitudine
        eventPublisher.publishEvent(new HabitCreatedEvent(this, savedHabit, currentUser));

        return savedHabit;
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
// completa abitudine

    public HabitCompletion completeHabit(UUID habitId, User user) {
       Habits habit = findById(habitId);
      
        HabitCompletion completion = new HabitCompletion(habit, user, LocalDateTime.now());
       habitCompletionRepository.save(completion);
        habit.addCompletion(completion);
        habitsRepository.save(habit);

        // Aggiorna lo stato completato se necessario
        habit.setCompleted(true);
        habitsRepository.save(habit);

        return completion;
    }
    // get all completion
    public Page<HabitCompletion> getCompletions(int pageNumber, int pageSize, String sortBy, User user) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return habitCompletionRepository.findByUserId(user.getId(), pageable);
    }


    public Habits updateHabits(UUID id, HabitsDTO payload, User user) {
        Habits found = this.findById(id);

        if (!found.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("You are not authorized to modify this habit.");
        }

        found.setName(payload.name());
        found.setFrequency(convertStringToFrequency(payload.frequency()));
        found.setUpdatedAt(LocalDateTime.now());

        if (payload.category() != null) {
            Category category = categoryService.findByName(payload.category());
            found.setCategory(category);
        }

        Habits updatedHabit = habitsRepository.save(found);
        // Pubblica un evento per notificare l'aggiornamento dell'abitudine
        eventPublisher.publishEvent(new HabitCreatedEvent(this, updatedHabit, user));

        return updatedHabit;
    }


    public List<Habits> saveHabitsByFrequency(HabitsDTO body) {
        User found = this.userService.findById(body.user());
        Frequency frequency = convertStringToFrequency(body.frequency());
        List<Habits> createdHabits = new ArrayList<>();

        // Logica per creare abitudini basate sulla frequenza
        switch (frequency) {
            case EVERYDAY:
                for (int i = 0; i < 7; i++) { // Crea 7 abitudini giornaliere
                    Habits habit = new Habits(
                            body.name(),
                            frequency,
                            body.reminder(),
                            LocalDateTime.now().plusDays(i),
                            LocalDateTime.now(),
                            body.completed(),
                            categoryService.findByName(body.category()),
                            found
                    );
                    Habits savedHabit = habitsRepository.save(habit);
                    createdHabits.add(savedHabit);
                    eventPublisher.publishEvent(new HabitCreatedEvent(this, savedHabit, found));
                }
                break;
            case EVERY3DAYS:
                for (int i = 0; i < 4; i++) { // Crea 4 abitudini ogni 3 giorni
                    Habits habit = new Habits(
                            body.name(),
                            frequency,
                            body.reminder(),
                            LocalDateTime.now().plusDays(i * 3),
                           LocalDateTime.now(),
                            body.completed(),
                            categoryService.findByName(body.category()),
                            found
                    );
                    Habits savedHabit = habitsRepository.save(habit);
                    createdHabits.add(savedHabit);
                    // Pubblica un evento per ogni abitudine creata
                    eventPublisher.publishEvent(new HabitCreatedEvent(this, savedHabit, found));

                }
                break;
            case ONCEAWEEK:
                for (int i = 0; i < 12; i++) { // Crea 12 abitudini settimanali
                    Habits habit = new Habits(
                            body.name(),
                            frequency,
                            body.reminder(),
                            LocalDateTime.now().plusWeeks(i),
                            LocalDateTime.now(),
                            body.completed(),
                            categoryService.findByName(body.category()),
                            found
                    );
                    Habits savedHabit = habitsRepository.save(habit);
                    createdHabits.add(savedHabit);
                    // Pubblica un evento per ogni abitudine creata
                    eventPublisher.publishEvent(new HabitCreatedEvent(this, savedHabit, found));
                }
                break;
            case ONCEAMONTH:
                for (int i = 0; i < 12; i++) { // Crea 12 abitudini mensili
                    Habits habit = new Habits(
                            body.name(),
                            frequency,
                            body.reminder(),
                            LocalDateTime.now().plusMonths(i),
                           LocalDateTime.now(),
                            body.completed(),
                            categoryService.findByName(body.category()),
                            found
                    );
                    Habits savedHabit = habitsRepository.save(habit);
                    createdHabits.add(savedHabit);
                    // Pubblica un evento per ogni abitudine creata
                    eventPublisher.publishEvent(new HabitCreatedEvent(this, savedHabit, found));
                }
                break;
            default:
                throw new BadRequestException("Unsupported frequency type");
        }

        return createdHabits;
    }


}
