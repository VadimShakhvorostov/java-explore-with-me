package ru.practicum.main.controller.publi;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.compilation.CompilationResponse;
import ru.practicum.main.service.compilations.CompilationsService;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class PublicCompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping("/compilations")
    public List<CompilationResponse> getCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return compilationsService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationResponse getCompilationById(@PathVariable Long compId) {
        return compilationsService.getCompilation(compId);
    }

}
