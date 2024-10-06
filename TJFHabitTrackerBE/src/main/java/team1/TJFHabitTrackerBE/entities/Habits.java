package team1.TJFHabitTrackerBE.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import team1.TJFHabitTrackerBE.enums.Frequency;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "habits")
@Getter
@Setter
@NoArgsConstructor
@ToString (exclude = {"users", "habitCompletions"})
public class Habits {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    private boolean reminder;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    private boolean completed;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "user_habits",
            joinColumns = @JoinColumn(name = "habit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitCompletion> habitCompletions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
    // Aggiungi la lista di date di frequenza
    @ElementCollection
    @CollectionTable(name = "habit_dates", joinColumns = @JoinColumn(name = "habit_id"))
    @Column(name = "date")
    private List<LocalDateTime> frequencyDates = new ArrayList<>();
    public Habits(String name, Frequency frequency, boolean reminder, LocalDateTime createdAt, LocalDateTime updatedAt, boolean completed, Category category, User owner) {
        this.name = name;
        this.frequency = frequency;
        this.reminder = reminder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completed = completed;
        this.owner = owner;
        this.users.add(owner);
    }
    public void addUser(User user) {
        users.add(user);
        user.getHabits().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getHabits().remove(this);
    }

    // Metodi per gestire le completazioni
    public void addCompletion(HabitCompletion completion) {
        habitCompletions.add(completion);
        completion.setHabit(this);
    }

    public void removeCompletion(HabitCompletion completion) {
        habitCompletions.remove(completion);
        completion.setHabit(null);
    }
}




