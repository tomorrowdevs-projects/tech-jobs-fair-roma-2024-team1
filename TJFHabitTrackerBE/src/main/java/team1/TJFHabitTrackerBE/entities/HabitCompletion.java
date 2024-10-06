package team1.TJFHabitTrackerBE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "habit_completions")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HabitCompletion {
    @Id
    @GeneratedValue
    private UUID id;
@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habits habit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public HabitCompletion(Habits habit, User user, LocalDateTime completedAt) {
        this.habit = habit;
        this.user = user;
        this.completedAt = completedAt;
    }
}
