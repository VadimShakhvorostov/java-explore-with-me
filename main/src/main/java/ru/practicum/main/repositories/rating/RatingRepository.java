package ru.practicum.main.repositories.rating;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.enums.VoteType;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    Optional<RatingEntity> findByUserIdAndEventId(long userId, long eventId);

    @Query("""
            select r
            from RatingEntity r
            where r.event.id = ?1
            and r.type = ?2
            """)
    List<RatingEntity> getRatingEntitiesByEventId(long eventId, VoteType voteType, Pageable pageable);
}