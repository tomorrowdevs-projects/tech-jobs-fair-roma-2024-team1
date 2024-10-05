package team1.TJFHabitTrackerBE.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team1.TJFHabitTrackerBE.entities.Habits;

import java.time.LocalDate;
import java.util.UUID;
@Repository
public interface HabitsRepository extends JpaRepository<Habits, UUID> {




    // Abitudini proprietarie e completate
    Page<Habits> findByOwnerIdAndCompleted(String ownerId, boolean completed, Pageable pageable);

    // Abitudini condivise e completate
    Page<Habits> findByUsers_IdAndCompleted(String userId, boolean completed, Pageable pageable);

    // Abitudini proprietarie o condivise e completate
    @Query("SELECT h FROM Habits h JOIN h.users u WHERE h.owner.id = :userId OR u.id = :userId AND h.completed = :completed")
    Page<Habits> findByOwnerIdOrUsers_IdAndCompleted(@Param("userId") String userId, @Param("completed") boolean completed, Pageable pageable);
}
