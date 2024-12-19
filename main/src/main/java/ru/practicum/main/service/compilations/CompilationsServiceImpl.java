package ru.practicum.main.service.compilations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.compilation.CompilationRequest;
import ru.practicum.main.dto.compilation.CompilationResponse;
import ru.practicum.main.dto.compilation.CompilationUpdateRequest;
import ru.practicum.main.dto.compilation.mapper.CompilationsMapper;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.repositories.compilation.CompilationEntity;
import ru.practicum.main.repositories.compilation.CompilationsRepository;
import ru.practicum.main.repositories.events.EventEntity;
import ru.practicum.main.repositories.events.EventsRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;
    private final CompilationsMapper compilationsMapper;

    @Override
    @Transactional
    public CompilationResponse addNewCompilations(CompilationRequest compilationRequest) {

        List<EventEntity> eventEntities = new ArrayList<>();
        if (compilationRequest.getEvents() != null && !compilationRequest.getEvents().isEmpty()) {
            eventEntities = eventsRepository.findAllById(compilationRequest.getEvents());
        }
        CompilationEntity compilationEntity = new CompilationEntity();
        compilationEntity.setEvents(new HashSet<>(eventEntities));
        compilationEntity.setPinned(compilationRequest.getPinned() == null ? false : true);
        compilationEntity.setTitle(compilationRequest.getTitle());

        CompilationEntity savedCompilation = compilationsRepository.save(compilationEntity);

        return compilationsMapper.toCompilationResponse(savedCompilation);
    }

    @Override
    @Transactional
    public void deleteCompilations(Long compId) {
        compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        compilationsRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationResponse updateCompilations(long compId, CompilationUpdateRequest compilationRequest) {
        CompilationEntity compilationEntity = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        List<EventEntity> eventEntities;
        if (compilationRequest.getEvents() != null) {
            eventEntities = eventsRepository.findAllById(compilationRequest.getEvents());
            compilationEntity.setEvents(new HashSet<>(eventEntities));
        }

        compilationEntity.setTitle(compilationRequest.getTitle() == null
                ? compilationEntity.getTitle() : compilationRequest.getTitle());

        compilationEntity.setPinned(compilationRequest.getPinned() == null
                ? compilationEntity.getPinned() : compilationRequest.getPinned());

        return compilationsMapper.toCompilationResponse(compilationsRepository.save(compilationEntity));
    }

    @Override
    public List<CompilationResponse> getCompilations(boolean pinned, int from, int size) {
        return compilationsRepository.findByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map(compilationsMapper::toCompilationResponse)
                .toList();
    }

    @Override
    public CompilationResponse getCompilation(long compId) {
        CompilationEntity compilationEntity = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        return compilationsMapper.toCompilationResponse(compilationEntity);
    }
}
