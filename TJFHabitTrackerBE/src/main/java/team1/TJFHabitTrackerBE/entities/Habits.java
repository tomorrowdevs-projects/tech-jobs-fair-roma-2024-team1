package team1.TJFHabitTrackerBE.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import team1.TJFHabitTrackerBE.enums.Frequency;

import java.util.UUID;

@Entity
@Table(name = "habits")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Habits {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    private boolean reminder;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Habits(String name, Frequency frequency, boolean reminder, User user) {
        this.name = name;
        this.frequency = frequency;
        this.reminder = reminder;
        this.user = user;
    }
}




