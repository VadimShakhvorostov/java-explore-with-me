package ru.practicum.main.controller.publi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.events.EventResponse;
import ru.practicum.main.enums.Sort;
import ru.practicum.main.service.events.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class PublicEventsController {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getPublicEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) Sort sort,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") @PositiveOrZero int size,
            HttpServletRequest httpServletRequest) {

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, dateTimeFormatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, dateTimeFormatter) : null;

        return eventService.getPublicEvents(text, categories, paid, start, end, onlyAvailable, sort, from, size, httpServletRequest);
    }

    @GetMapping("/{eventId}")
    public EventResponse getEventsById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        return eventService.getEventById(eventId, httpServletRequest);
    }
}
