package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.events.EventResponse;
import ru.practicum.main.dto.events.EventUpdateRequestAdmin;
import ru.practicum.main.enums.States;
import ru.practicum.main.service.events.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminEventsController {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;

    @GetMapping("/events")
    public List<EventResponse> getAdminEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<States> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") @PositiveOrZero int size
    ) {

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, dateTimeFormatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, dateTimeFormatter) : null;

        return eventService.getAdminEvents(
                users,
                states,
                categories,
                start,
                end,
                from,
                size);
    }

    @PatchMapping("/events/{eventId}")
    public EventResponse updateEvent(
            @PathVariable long eventId,
            @RequestBody @Valid EventUpdateRequestAdmin eventUpdateRequestAdmin) {
        return eventService.updateEvent(eventId, eventUpdateRequestAdmin);
    }
}
