package ru.practicum.main.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.events.EventRequest;
import ru.practicum.main.dto.events.EventResponse;
import ru.practicum.main.dto.events.EventSimpleResponse;
import ru.practicum.main.dto.events.EventUpdateRequestUser;
import ru.practicum.main.service.events.EventService;

import java.util.List;


@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class PrivateEventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventSimpleResponse> getEventsByUserId(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size) {

        return eventService.getEventsByUserId(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventResponse addEvent(
            @PathVariable long userId,
            @RequestBody @Valid EventRequest eventRequest) {
        return eventService.addEvent(userId, eventRequest);
    }

    @GetMapping("/{eventId}")
    public EventResponse getFullEventByUserId(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getFullEventByUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventResponse updateOwnerEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody @Valid EventUpdateRequestUser eventUpdateRequestUser) {
        return eventService.updateOwnerEvent(userId, eventId, eventUpdateRequestUser);
    }
}
