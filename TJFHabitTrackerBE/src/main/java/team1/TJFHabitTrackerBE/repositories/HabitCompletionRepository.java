package team1.TJFHabitTrackerBE.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.TJFHabitTrackerBE.entities.HabitCompletion;

import java.util.UUID;
@Repository
public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, UUID> {
    Page<HabitCompletion> findByUserId(String userId, Pageable pageable);
    Page<HabitCompletion> findByHabitId(UUID habitId, Pageable pageable);
}
