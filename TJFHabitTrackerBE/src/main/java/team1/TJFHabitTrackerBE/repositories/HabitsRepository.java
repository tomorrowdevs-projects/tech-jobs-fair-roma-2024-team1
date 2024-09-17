package team1.TJFHabitTrackerBE.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team1.TJFHabitTrackerBE.entities.Habits;

import java.time.LocalDate;
import java.util.UUID;

public interface HabitsRepository extends JpaRepository<Habits, UUID> {

    Page<Habits> findByCompleted (boolean completed, Pageable pageable);
}
