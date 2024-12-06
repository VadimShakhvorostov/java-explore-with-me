package ru.practicum.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.server.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class StatsController {

    private final StatsServiceImpl statsService;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public void addHit(@RequestBody HitDto hitDto) {
        statsService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        LocalDateTime startDT;
        LocalDateTime endDT;

        try {
            startDT = LocalDateTime.parse(start, dateFormat);
            endDT = LocalDateTime.parse(end, dateFormat);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
        return statsService
                .getStats(startDT, endDT, uris, unique);
    }
}
