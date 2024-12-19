package ru.practicum.main.service.requests;

import ru.practicum.main.dto.request.RequestDtoResponse;
import ru.practicum.main.dto.request.RequestListResponse;
import ru.practicum.main.dto.request.RequestRequest;

import java.util.List;

public interface RequestService {

    RequestDtoResponse addRequestUser(Long userId, Long eventId);

    List<RequestDtoResponse> getRequestUser(long userId);

    List<RequestDtoResponse> getRequestsEventUsers(long userId, long eventId);

    RequestDtoResponse cancelRequestUser(long userId, long requestId);

    RequestListResponse changeStatusRequestUser(long userId, long eventId, RequestRequest requestRequest);
}

