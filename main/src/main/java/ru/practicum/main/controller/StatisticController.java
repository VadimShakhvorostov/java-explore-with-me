package ru.practicum.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;

import java.util.List;

@RestController
@RequestMapping
public class StatisticController {

    @Autowired
    StatsClient statsClient;

    @PostMapping("/hit")
    public void addHit(@RequestBody HitDto hitDto) {
        statsClient.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        return statsClient.getStats(start, end, uris, unique);
    }
}
