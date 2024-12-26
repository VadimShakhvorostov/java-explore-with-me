package ru.practicum.main.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.dto.categories.CategoryResponse;
import ru.practicum.main.dto.location.LocationDto;
import ru.practicum.main.dto.users.UserResponse;
import ru.practicum.main.enums.States;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private String annotation;
    private CategoryResponse category;
    private long confirmedRequests;

    private String createdOn;
    private String description;
    private String eventDate;
    private long id;

    private UserResponse initiator;
    private LocationDto location;
    private boolean paid;

    private long participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;

    private States state;
    private String title;
    private long views;

    private long likeCount;
    private long dislikeCount;
    private float rating;
}
