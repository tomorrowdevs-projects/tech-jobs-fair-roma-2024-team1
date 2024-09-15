package team1.TJFHabitTrackerBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.TJFHabitTrackerBE.entities.Habits;

import java.util.UUID;

public interface HabitsRepository extends JpaRepository<Habits, UUID> {
}
