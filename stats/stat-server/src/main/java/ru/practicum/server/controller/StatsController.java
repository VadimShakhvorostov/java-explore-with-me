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
import ru.practicum.server.util.DateFormat;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class StatsController {

    private final StatsServiceImpl statsService;

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
        return statsService
                .getStats(DateFormat.toLocaleDateTime(start), DateFormat.toLocaleDateTime(end), uris, unique);
    }
}
