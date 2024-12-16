package ru.practicum.main.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.dto.categories.CategoriesResponse;
import ru.practicum.main.dto.users.UserPrivateResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSimpleResponse {

    private String annotation;
    private CategoriesResponse category;
    private long confirmedRequests;

    private String eventDate;
    private long id;
    private UserPrivateResponse initiator;

    private boolean paid;
    private String title;
    private long views;

}