package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.compilation.CompilationRequest;
import ru.practicum.main.dto.compilation.CompilationResponse;
import ru.practicum.main.dto.compilation.CompilationUpdateRequest;
import ru.practicum.main.service.compilations.CompilationsService;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class AdminCompilationsController {

    private final CompilationsService compilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponse addCompilations(@RequestBody @Valid CompilationRequest compilationsDto) {
        return compilationsService.addNewCompilations(compilationsDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilations(@PathVariable Long compId) {
        compilationsService.deleteCompilations(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationResponse updateCompilation(@PathVariable Long compId, @RequestBody @Valid CompilationUpdateRequest compilationRequest) {
        return compilationsService.updateCompilations(compId, compilationRequest);
    }
}
