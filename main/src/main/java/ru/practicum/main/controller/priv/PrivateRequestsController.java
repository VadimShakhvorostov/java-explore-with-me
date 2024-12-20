package ru.practicum.main.controller.priv;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.request.RequestDtoResponse;
import ru.practicum.main.dto.request.RequestListResponse;
import ru.practicum.main.dto.request.RequestRequest;
import ru.practicum.main.service.requests.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class PrivateRequestsController {

    private final RequestService requestService;


    @PatchMapping("/events/{eventId}/requests")
    public RequestListResponse changeStatusRequestUser(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody RequestRequest requestRequest) {
        return requestService.changeStatusRequestUser(userId, eventId, requestRequest);
    }

    @GetMapping("/requests")
    public List<RequestDtoResponse> getRequestUser(@PathVariable long userId) {
        return requestService.getRequestUser(userId);
    }


    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDtoResponse addRequestUser(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        return requestService.addRequestUser(userId, eventId);
    }


    @GetMapping("events/{eventId}/requests")
    public List<RequestDtoResponse> getRequestsEventUsers(
            @PathVariable long userId,
            @PathVariable long eventId) {
        return requestService.getRequestsEventUsers(userId, eventId);
    }

    @PatchMapping("requests/{requestId}/cancel")
    public RequestDtoResponse cancelRequestUser(
            @PathVariable long userId,
            @PathVariable long requestId
    ) {
        return requestService.cancelRequestUser(userId, requestId);
    }


}
