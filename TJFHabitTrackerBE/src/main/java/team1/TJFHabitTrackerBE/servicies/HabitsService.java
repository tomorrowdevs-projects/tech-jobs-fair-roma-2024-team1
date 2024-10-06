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
// save habit
public Habits saveHabits(HabitsDTO body, String currentUserId) {
    // Trova l'utente corrente
    User currentUser = userService.findById(currentUserId);
    if (currentUser == null) {
        throw new NotFoundException("User not found with ID: " + currentUserId);
    }

    // Trova la categoria
    Category category = categoryService.findByName(body.category());
    if (category == null) {
        throw new NotFoundException("Category not found: " + body.category());
    }

    // Converti la stringa di frequenza in enum
    Frequency frequency = null;
    if (body.frequency() != null && !body.frequency().isEmpty()) {
        frequency = convertStringToFrequency(body.frequency());
    }

    // Crea l'istanza di Habits
    Habits habit = new Habits(
            body.name(),
            frequency,
            body.reminder(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            body.completed(),
            category,
            currentUser
    );

    // Genera le date di frequenza basate sulla frequenza selezionata
    if (frequency != null) {
        generateFrequencyDates(habit, frequency);
    }

    // Aggiungi altri utenti come collaboratori


    // Salva l'abitudine nel repository
    Habits savedHabit = habitsRepository.save(habit);

    // Pubblica un evento di creazione abitudine
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
        // Trova l'abitudine per ID
        Habits found = this.findById(id);

        // Verifica se l'utente è il proprietario dell'abitudine
        if (!found.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("You are not authorized to modify this habit.");
        }

        // Aggiorna il nome dell'abitudine
        if (payload.name() != null && !payload.name().trim().isEmpty()) {
            found.setName(payload.name());
        }

        // Aggiorna la frequenza se fornita
        if (payload.frequency() != null && !payload.frequency().isEmpty()) {
            Frequency newFrequency = convertStringToFrequency(payload.frequency());

            // Se la frequenza è cambiata, aggiorna la lista di frequencyDates
            if (!newFrequency.equals(found.getFrequency())) {
                found.setFrequency(newFrequency);
                found.getFrequencyDates().clear(); // Pulisci le date esistenti
                generateFrequencyDates(found, newFrequency); // Rigenera le date basate sulla nuova frequenza
            }
        }

        // Aggiorna il reminder
        found.setReminder(payload.reminder());

        // Aggiorna il completato
        found.setCompleted(payload.completed());

        // Aggiorna la categoria se fornita
        if (payload.category() != null && !payload.category().trim().isEmpty()) {
            Category category = categoryService.findByName(payload.category());
            if (category == null) {
                throw new NotFoundException("Category not found: " + payload.category());
            }
            found.setCategory(category);
        }



        // Aggiorna la data di aggiornamento
        found.setUpdatedAt(LocalDateTime.now());




        return habitsRepository.save(found);
    }

// creazione date per frequenza
private void generateFrequencyDates(Habits habit, Frequency frequency) {
    LocalDateTime now = LocalDateTime.now();
    switch (frequency) {
        case EVERYDAY:
            for (int i = 0; i < 7; i++) { // 7 giorni
                habit.getFrequencyDates().add(now.plusDays(i));
            }
            break;
        case EVERY3DAYS:
            for (int i = 0; i < 4; i++) { // Ogni 3 giorni, 4 occorrenze
                habit.getFrequencyDates().add(now.plusDays(i * 3));
            }
            break;
        case ONCEAWEEK:
            for (int i = 0; i < 12; i++) { // 12 settimane
                habit.getFrequencyDates().add(now.plusWeeks(i));
            }
            break;
        case ONCEAMONTH:
            for (int i = 0; i < 12; i++) { // 12 mesi
                habit.getFrequencyDates().add(now.plusMonths(i));
            }
            break;
        default:
            throw new BadRequestException("Unsupported frequency type");
    }
}



}
