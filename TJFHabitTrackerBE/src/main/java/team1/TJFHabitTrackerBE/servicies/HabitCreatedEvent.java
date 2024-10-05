package team1.TJFHabitTrackerBE.servicies;

import org.springframework.context.ApplicationEvent;
import team1.TJFHabitTrackerBE.entities.Habits;
import team1.TJFHabitTrackerBE.entities.User;

public class HabitCreatedEvent extends ApplicationEvent {
    private final Habits habit;
    private final User user;

    public HabitCreatedEvent(Object source, Habits habit, User user) {
        super(source);
        this.habit = habit;
        this.user = user;
    }

    public Habits getHabit() {
        return habit;
    }

    public User getUser() {
        return user;
    }
}
