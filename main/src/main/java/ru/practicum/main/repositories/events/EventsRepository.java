package ru.practicum.main.repositories.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<EventEntity, Long> {

    @Query("""
            select ev
            from EventEntity ev
            where ev.initiator.id = ?1
            """
    )
    List<EventEntity> getEventsByUserId(long userId, Pageable pageable);

    Optional<EventEntity> getEventByIdAndInitiator_Id(long id, long initiatorId);

    boolean existsByIdAndInitiator_Id(Long eventId, long initiator);

    boolean existsByCategory_Id(long catId);
}


