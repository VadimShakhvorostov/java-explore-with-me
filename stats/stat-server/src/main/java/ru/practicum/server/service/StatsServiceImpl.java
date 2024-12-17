package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.server.exception.DateTimeException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.mapper.StatsMapper;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;
    private final StatsMapper statsMapper;

    @Override
    public void addHit(HitDto hitDto) {
        statsRepository.save(hitMapper.toHitEntity(hitDto));
    }

    @Override
    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (start.isAfter(end)) {
            throw new DateTimeException("Start date cannot be after end date");
        }

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository
                        .findAllWithoutUrisUnique(start, end)
                        .stream()
                        .map(statsMapper::toStatDto)
                        .toList();
            } else {
                return statsRepository
                        .findAllWithoutUrisNotUnique(start, end)
                        .stream()
                        .map(statsMapper::toStatDto)
                        .toList();
            }
        } else {
            if (unique) {
                return statsRepository
                        .findAllWithUrisUnique(start, end, uris)
                        .stream()
                        .map(statsMapper::toStatDto)
                        .toList();
            } else {
                return statsRepository
                        .findAllWithUrisNotUnique(start, end, uris)
                        .stream()
                        .map(statsMapper::toStatDto)
                        .toList();
            }
        }
    }
}
