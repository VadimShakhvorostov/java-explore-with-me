package ru.practicum.main.service.requests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.request.RequestDtoResponse;
import ru.practicum.main.dto.request.RequestListResponse;
import ru.practicum.main.dto.request.RequestRequest;
import ru.practicum.main.dto.request.mapper.RequestMapper;
import ru.practicum.main.enums.States;

import ru.practicum.main.enums.Status;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.RequestException;
import ru.practicum.main.repositories.events.EventEntity;
import ru.practicum.main.repositories.events.EventsRepository;
import ru.practicum.main.repositories.request.RequestEntity;
import ru.practicum.main.repositories.request.RequestRepository;
import ru.practicum.main.repositories.users.UserEntity;
import ru.practicum.main.repositories.users.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;
    private final RequestMapper requestMapper;

    @Override
    public RequestDtoResponse addRequestUser(Long userId, Long eventId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        EventEntity eventEntity = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        if (requestRepository.existsByEventAndRequester(eventId, userId)) {
            throw new RequestException("Repeated request");
        }

        if (eventsRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new RequestException("Self request");
        }

        if (!eventEntity.getState().equals(States.PUBLISHED)) {
            throw new RequestException("Not published");
        }


        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequester(userEntity.getId());
        requestEntity.setEvent(eventEntity.getId());
        requestEntity.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        boolean isConfirm = (eventEntity.getParticipantLimit().equals(eventEntity.getConfirmedRequests()));

        if (eventEntity.getParticipantLimit() != 0 && eventEntity.getRequestModeration().equals(true)) {
            if (isConfirm) {
                throw new RequestException("limit");
            }
            requestEntity.setStatus(Status.PENDING);
        }

        if (eventEntity.getParticipantLimit() == 0 && eventEntity.getRequestModeration().equals(true)) {
            requestEntity.setStatus(Status.CONFIRMED);
        }

        if (eventEntity.getParticipantLimit() == 0 && eventEntity.getRequestModeration().equals(false)) {
            requestEntity.setStatus(Status.CONFIRMED);
            eventEntity.setConfirmedRequests(eventEntity.getConfirmedRequests() + 1);
            eventsRepository.save(eventEntity);
        }


        if (eventEntity.getParticipantLimit() != 0 && eventEntity.getRequestModeration().equals(false)) {
            if (isConfirm) {
                throw new RequestException("The participant limit has been reached");
            }
            requestEntity.setStatus(Status.CONFIRMED);
            eventEntity.setConfirmedRequests(eventEntity.getConfirmedRequests() + 1);
            eventsRepository.save(eventEntity);
        }


        return requestMapper.toRequestDto(requestRepository.save(requestEntity));
    }

    @Override
    public List<RequestDtoResponse> getRequestUser(long userId) {
        validateUserId(userId);
        return requestRepository.findAllByRequester(userId).stream().map(requestMapper::toRequestDto).toList();
    }

    @Override
    public List<RequestDtoResponse> getRequestsEventUsers(long userId, long eventId) {
        if (!eventsRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new NotFoundException("Event with id " + eventId + " not found");
        }
        return requestRepository.findAllByEvent(eventId).stream().map(requestMapper::toRequestDto).toList();
    }

    @Override
    public RequestDtoResponse cancelRequestUser(long userId, long requestId) {
        validateUserId(userId);
        RequestEntity requestEntity = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));
        if (requestEntity.getRequester() != userId) {
            throw new RequestException("not authorized");
        }

        requestEntity.setStatus(Status.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(requestEntity));
    }

    @Override
    public RequestListResponse changeStatusRequestUser(long userId, long eventId, RequestRequest requestRequest) {
        validateUserId(userId);
        EventEntity eventEntity = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        if (eventEntity.getParticipantLimit() == 0 || !eventEntity.getRequestModeration()) {
            return new RequestListResponse();
        }

        if (eventEntity.getConfirmedRequests() >= eventEntity.getParticipantLimit()) {
            throw new RequestException("Limit exceeded");
        }

        List<RequestEntity> requestEntity = requestRepository.findAllById(requestRequest.getRequestIds());

        if (requestEntity.stream().anyMatch(request -> request.getStatus() != Status.PENDING)) {
            throw new RequestException("Not authorized");
        }

        List<RequestDtoResponse> confirmedRequests = new ArrayList<>();
        List<RequestDtoResponse> rejectedRequests = new ArrayList<>();

        if (requestRequest.getStatus() == Status.CONFIRMED) {
            for (RequestEntity request : requestEntity) {

                if (eventEntity.getConfirmedRequests() < eventEntity.getParticipantLimit()) {
                    request.setStatus(Status.CONFIRMED);
                    confirmedRequests.add(requestMapper.toRequestDto(request));
                    eventEntity.setConfirmedRequests(eventEntity.getConfirmedRequests() + 1);
                } else {
                    request.setStatus(Status.REJECTED);
                    rejectedRequests.add(requestMapper.toRequestDto(request));
                }
            }
        } else {
            for (RequestEntity request : requestEntity) {
                request.setStatus(Status.REJECTED);
                rejectedRequests.add(requestMapper.toRequestDto(request));
            }
        }

        requestRepository.saveAll(requestEntity);
        eventsRepository.save(eventEntity);

        return new RequestListResponse(confirmedRequests, rejectedRequests);
    }

    private void validateUserId(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }


}
