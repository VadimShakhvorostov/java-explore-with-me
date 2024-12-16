package ru.practicum.main.dto.events.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.dto.categories.CategoriesResponse;
import ru.practicum.main.dto.events.EventSimpleResponse;
import ru.practicum.main.dto.events.EventRequest;
import ru.practicum.main.dto.events.EventResponse;
import ru.practicum.main.dto.location.LocationDto;
import ru.practicum.main.dto.users.UserPrivateResponse;
import ru.practicum.main.dto.users.UserResponse;
import ru.practicum.main.enums.States;
import ru.practicum.main.repositories.categories.CategoriesEntity;
import ru.practicum.main.repositories.events.EventEntity;
import ru.practicum.main.repositories.users.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class EventMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventEntity toEventEntity(EventRequest eventRequest, CategoriesEntity categoriesEntity, UserEntity userEntity) {
        EventEntity eventEntity = new EventEntity();

        eventEntity.setAnnotation(eventRequest.getAnnotation());
        eventEntity.setCategory(categoriesEntity);

        eventEntity.setConfirmedRequests(0L);
        eventEntity.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        eventEntity.setDescription(eventRequest.getDescription());
        eventEntity.setEventDate(eventRequest.getEventDate());

        eventEntity.setInitiator(userEntity);

        eventEntity.setLon(eventRequest.getLocation() != null ? eventRequest.getLocation().getLon() : 0);
        eventEntity.setLat(eventRequest.getLocation() != null ? eventRequest.getLocation().getLat() : 0);


        eventEntity.setPaid(eventRequest.getPaid() != null ? eventRequest.getPaid() : false);

        eventEntity.setParticipantLimit(eventRequest.getParticipantLimit() != null ? eventRequest.getParticipantLimit() : 0);


        eventEntity.setPublishedOn(LocalDateTime.now());
        eventEntity.setRequestModeration(eventRequest.getRequestModeration() != null ? eventRequest.getRequestModeration() : true);

        eventEntity.setState(States.PENDING);
        eventEntity.setTitle(eventRequest.getTitle());
        eventEntity.setViews(0L);

        return eventEntity;
    }

    public EventResponse toEventResponse(EventEntity eventEntity) {
        EventResponse eventResponse = new EventResponse();

        eventResponse.setAnnotation(eventEntity.getAnnotation());
        eventResponse.setCategory(new CategoriesResponse(eventEntity.getCategory().getId(), eventEntity.getCategory().getName()));
        eventResponse.setConfirmedRequests(eventEntity.getConfirmedRequests());

        eventResponse.setCreatedOn(eventEntity.getCreatedOn().format(formatter));
        eventResponse.setDescription(eventEntity.getDescription());
        eventResponse.setEventDate(eventEntity.getEventDate().format(formatter));
        eventResponse.setId(eventEntity.getId());

        eventResponse.setInitiator(new UserResponse(
                eventEntity.getInitiator().getId(),
                eventEntity.getInitiator().getName(),
                eventEntity.getInitiator().getEmail()));

        eventResponse.setLocation(new LocationDto(
                eventEntity.getLat(),
                eventEntity.getLon()));

        eventResponse.setPaid(eventEntity.getPaid());

        eventResponse.setParticipantLimit(eventEntity.getParticipantLimit());
        eventResponse.setPublishedOn(eventEntity.getPublishedOn());
        eventResponse.setRequestModeration(eventEntity.getRequestModeration());

        eventResponse.setState(eventEntity.getState());
        eventResponse.setTitle(eventEntity.getTitle());
        eventResponse.setViews(eventEntity.getViews());

        return eventResponse;
    }

    public EventSimpleResponse toEventSimpleResponse(EventEntity eventEntity) {
        EventSimpleResponse response = new EventSimpleResponse();

        response.setAnnotation(eventEntity.getAnnotation());
        response.setCategory(new CategoriesResponse(
                eventEntity.getCategory().getId(),
                eventEntity.getCategory().getName()));
        response.setConfirmedRequests(eventEntity.getConfirmedRequests());

        response.setEventDate(eventEntity.getEventDate().format(formatter));
        response.setId(eventEntity.getId());
        response.setInitiator(new UserPrivateResponse(
                eventEntity.getInitiator().getId(),
                eventEntity.getInitiator().getName()
        ));

        response.setPaid(eventEntity.getPaid());
        response.setTitle(eventEntity.getTitle());
        response.setViews(eventEntity.getViews());

        return response;
    }


}
