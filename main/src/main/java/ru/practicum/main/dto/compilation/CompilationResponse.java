package ru.practicum.main.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.dto.events.EventResponse;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponse {
    private Long id;
    private Set<EventResponse> events;
    private Boolean pinned;
    private String title;
}
