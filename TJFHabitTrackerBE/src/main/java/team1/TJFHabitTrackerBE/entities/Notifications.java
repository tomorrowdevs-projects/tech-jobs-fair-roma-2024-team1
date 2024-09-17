package team1.TJFHabitTrackerBE.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Notifications {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habits habits;
    private String message;
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;
    @Column(name = "sent_at")
    private LocalDateTime sent_at;

    public Notifications(User user, Habits habits, String message, LocalDateTime scheduledAt, LocalDateTime sent_at) {
        this.user = user;
        this.habits = habits;
        this.message = message;
        this.scheduledAt = scheduledAt;
        this.sent_at = sent_at;
    }
}
