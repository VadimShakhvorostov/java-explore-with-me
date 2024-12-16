package ru.practicum.main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestListResponse {
    private List<RequestDtoResponse> confirmedRequests;
    private List<RequestDtoResponse> rejectedRequests;
}

