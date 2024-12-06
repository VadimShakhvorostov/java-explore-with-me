package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.entity.HitEntity;
import ru.practicum.server.entity.StatEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<HitEntity, Long> {

    @Query("""
            Select new ru.practicum.server.entity.StatEntity(h.app, h.uri, count(distinct h.ip))
            from HitEntity h
            WHERE h.timestamp between ?1 and ?2
            and h.uri in (?3)
            group by h.app, h.uri
            order by count(distinct h.ip) desc
            """)
    List<StatEntity> findAllWithUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            Select new ru.practicum.server.entity.StatEntity(h.app, h.uri, count(h.ip))
            from HitEntity h
            WHERE h.timestamp between ?1 and ?2
            and h.uri in (?3)
            group by h.app, h.uri
            order by count( h.ip) desc
            """)
    List<StatEntity> findAllWithUrisNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            Select new ru.practicum.server.entity.StatEntity(h.app, h.uri, count(distinct h.ip))
            from HitEntity h
            WHERE h.timestamp between ?1 and ?2
            group by h.app, h.uri
            order by count(distinct h.ip) desc
            """)
    List<StatEntity> findAllWithoutUrisUnique(LocalDateTime start, LocalDateTime end);

    @Query("""
            Select new ru.practicum.server.entity.StatEntity(h.app, h.uri, count((h.ip)))
            from HitEntity h
            WHERE h.timestamp between ?1 and ?2
            group by h.app, h.uri
            order by count( h.ip) desc
            """)
    List<StatEntity> findAllWithoutUrisNotUnique(LocalDateTime start, LocalDateTime end);
}
