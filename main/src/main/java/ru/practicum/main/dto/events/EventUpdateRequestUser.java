package ru.practicum.main.dto.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.dto.location.LocationDto;
import ru.practicum.main.enums.StateActionUser;
import ru.practicum.main.util.DateTimeDeserializer;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventUpdateRequestUser {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @Positive
    private Long participantLimit;
    private Boolean requestModeration;
    private StateActionUser stateAction;
    @Size(min = 3, max = 120)
    private String title;
}










