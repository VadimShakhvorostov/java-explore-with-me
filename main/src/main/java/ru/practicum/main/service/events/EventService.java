package ru.practicum.main.service.events;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main.dto.events.EventRequest;
import ru.practicum.main.dto.events.EventResponse;
import ru.practicum.main.dto.events.EventSimpleResponse;
import ru.practicum.main.dto.events.EventUpdateRequestAdmin;
import ru.practicum.main.dto.events.EventUpdateRequestUser;
import ru.practicum.main.enums.Sort;
import ru.practicum.main.enums.States;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventResponse addEvent(long userId, EventRequest eventRequest);

    List<EventSimpleResponse> getEventsByUserId(long userId, int from, int size);

    EventResponse getFullEventByUserId(long userId, long eventId);


    List<EventResponse> getAdminEvents(List<Long> users, List<States> states, List<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<EventResponse> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                        Sort sort, int from, int size, HttpServletRequest httpServletRequest);

    EventResponse updateEvent(long eventId, EventUpdateRequestAdmin eventUpdateRequestAdmin);

    EventResponse getEventById(long eventId, HttpServletRequest httpServletRequest);

    EventResponse updateOwnerEvent(long userId, long eventId, EventUpdateRequestUser eventUpdateRequestUser);

}
