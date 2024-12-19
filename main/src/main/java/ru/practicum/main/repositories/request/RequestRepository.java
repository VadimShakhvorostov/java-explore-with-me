package ru.practicum.main.repositories.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {


    List<RequestEntity> findAllByRequester(long userId);

    List<RequestEntity> findAllByEvent(long eventId);

    boolean existsByEventAndRequester(long eventId, long userId);


}
