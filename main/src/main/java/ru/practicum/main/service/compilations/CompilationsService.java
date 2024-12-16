package ru.practicum.main.service.compilations;

import ru.practicum.main.dto.compilation.CompilationRequest;
import ru.practicum.main.dto.compilation.CompilationResponse;
import ru.practicum.main.dto.compilation.CompilationUpdateRequest;

import java.util.List;

public interface CompilationsService {
    CompilationResponse addNewCompilations(CompilationRequest compilationRequest);

    void deleteCompilations(Long compId);

    CompilationResponse updateCompilations(long compId, CompilationUpdateRequest compilationRequest);

    List<CompilationResponse> getCompilations(boolean pinned, int from, int size);

    CompilationResponse getCompilation(long compId);

}
