package ru.practicum.main.service.events;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.main.dto.events.EventSimpleResponse;
import ru.practicum.main.dto.events.EventRequest;
import ru.practicum.main.dto.events.EventResponse;
import ru.practicum.main.dto.events.EventUpdateRequestAdmin;
import ru.practicum.main.dto.events.EventUpdateRequestUser;
import ru.practicum.main.dto.events.mapper.EventMapper;
import ru.practicum.main.enums.Sort;
import ru.practicum.main.enums.StateActionAdmin;
import ru.practicum.main.enums.StateActionUser;
import ru.practicum.main.enums.States;
import ru.practicum.main.exception.DateException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.RequestException;
import ru.practicum.main.repositories.categories.CategoryEntity;
import ru.practicum.main.repositories.categories.CategoryRepository;
import ru.practicum.main.repositories.events.EventEntity;
import ru.practicum.main.repositories.events.EventsRepository;
import ru.practicum.main.repositories.rating.RatingRepository;
import ru.practicum.main.repositories.users.UserEntity;
import ru.practicum.main.repositories.users.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository catRepository;
    private final EntityManager entityManager;
    private final StatsClient statsClient;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public EventResponse addEvent(long userId, EventRequest eventRequest) {
        if (eventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RequestException("Event date is earlier than 2 hours");
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        CategoryEntity categoryEntity = catRepository.findById(eventRequest.getCategory())
                .orElseThrow(() -> new NotFoundException("Category  with id=" + eventRequest.getCategory() + " was not found"));
        EventEntity eventEntity = eventMapper.toEventEntity(eventRequest, categoryEntity, userEntity);
        return eventMapper.toEventResponse(eventsRepository.save(eventEntity));
    }

    @Override
    public List<EventSimpleResponse> getEventsByUserId(long userId, int from, int size) {
        return eventsRepository.getEventsByUserId(userId, PageRequest.of(from, size))
                .stream()
                .map(eventMapper::toEventSimpleResponse)
                .toList();
    }

    @Override
    public EventResponse getFullEventByUserId(long userId, long eventId) {
        validateUserId(userId);
        EventEntity eventEntity = eventsRepository.getEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        return eventMapper.toEventResponse(eventEntity);
    }

    @Override
    public List<EventResponse> getAdminEvents(List<Long> users, List<States> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = criteriaBuilder.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            CriteriaBuilder.In<Long> usersPredicate = criteriaBuilder.in(root.get("initiator").get("id"));
            for (Long id : users) {
                usersPredicate.value(id);
            }
            predicates.add(usersPredicate);
        }

        if (states != null && !states.isEmpty()) {
            CriteriaBuilder.In<States> statesPredicate = criteriaBuilder.in(root.get("state"));
            for (States state : states) {
                statesPredicate.value(state);
            }
            predicates.add(statesPredicate);
        }

        if (categories != null && !categories.isEmpty()) {
            CriteriaBuilder.In<Long> categoriesPredicate = criteriaBuilder.in(root.get("category").get("id"));
            for (Long categoryId : categories) {
                categoriesPredicate.value(categoryId);
            }
            predicates.add(categoriesPredicate);
        }

        if (rangeStart != null && rangeEnd != null) {
            predicates.add(criteriaBuilder.between(root.get("eventDate"), rangeStart, rangeEnd));
        }

        query.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<EventEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);


        return typedQuery.getResultList().stream().map(eventMapper::toEventResponse).toList();

    }

    @Override
    @Transactional
    public EventResponse updateEvent(long eventId, EventUpdateRequestAdmin eventUpdateRequestAdmin) {

        log.info("updateEvent, id = {}, eventUpdateRequest = {} ", eventId, eventUpdateRequestAdmin);

        EventEntity eventEntity = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (eventUpdateRequestAdmin.getEventDate() != null
                && eventUpdateRequestAdmin.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DateException("Date is earlier than 2 hours");
        }
        if (!eventEntity.getState().equals(States.PENDING)
                && eventUpdateRequestAdmin.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
            throw new RequestException("Only events with the pending status can be published");
        }
        if (eventEntity.getState().equals(States.PUBLISHED)
                && eventUpdateRequestAdmin.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
            throw new RequestException("Published events cannot be rejected");
        }
        eventEntity.setAnnotation(eventUpdateRequestAdmin.getAnnotation() == null
                ? eventEntity.getAnnotation() : eventUpdateRequestAdmin.getAnnotation());

        if (eventUpdateRequestAdmin.getCategory() != null) {
            CategoryEntity categoryEntity = catRepository.findById(eventUpdateRequestAdmin.getCategory())
                    .orElseThrow(() -> new NotFoundException("Categories with id=" + eventUpdateRequestAdmin.getCategory() + " not found"));
            eventEntity.setCategory(eventUpdateRequestAdmin.getCategory() == null
                    ? eventEntity.getCategory() : categoryEntity);
        }

        eventEntity.setDescription(eventUpdateRequestAdmin.getDescription() == null
                ? eventEntity.getDescription() : eventUpdateRequestAdmin.getDescription());

        eventEntity.setEventDate(eventUpdateRequestAdmin.getEventDate() == null
                ? eventEntity.getEventDate() : eventUpdateRequestAdmin.getEventDate());

        if (eventUpdateRequestAdmin.getLocation() != null) {
            float lat = eventUpdateRequestAdmin.getLocation().getLat();
            float lon = eventUpdateRequestAdmin.getLocation().getLon();
            eventEntity.setLat(lat);
            eventEntity.setLon(lon);
        }

        if (eventUpdateRequestAdmin.getPaid() != null) {
            eventEntity.setPaid(eventUpdateRequestAdmin.getPaid());
        }

        eventEntity.setParticipantLimit(eventUpdateRequestAdmin.getParticipantLimit() == null
                ? eventEntity.getParticipantLimit() : eventUpdateRequestAdmin.getParticipantLimit());

        if (eventUpdateRequestAdmin.getRequestModeration() != null) {
            eventEntity.setRequestModeration(eventUpdateRequestAdmin.getRequestModeration());
        }

        eventEntity.setTitle(eventUpdateRequestAdmin.getTitle() == null
                ? eventEntity.getTitle() : eventUpdateRequestAdmin.getTitle());


        if (eventUpdateRequestAdmin.getStateAction() != null) {
            if (eventUpdateRequestAdmin.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                eventEntity.setState(States.PUBLISHED);
                eventEntity.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            } else {
                eventEntity.setState(States.CANCELED);
            }
        }

        return eventMapper.toEventResponse(eventsRepository.save(eventEntity));
    }

    @Override
    @Transactional
    public EventResponse updateOwnerEvent(long userId, long eventId, EventUpdateRequestUser eventUpdateRequestUser) {
        if (!eventsRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new NotFoundException("Event with id=" + eventId + " users with id=" + userId + " was not found");
        }
        validateUserId(userId);
        EventEntity eventEntity = eventsRepository
                .findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (eventEntity.getState().equals(States.PUBLISHED)) {
            throw new RequestException("Only pending or canceled events can be changed");
        }

        if (eventEntity.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DateException("Event date is earlier than 2 hours");
        }

        if (eventUpdateRequestUser.getEventDate() != null &&
                eventUpdateRequestUser.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DateException("Date is earlier than 2 hours");
        }

        eventEntity.setAnnotation(eventUpdateRequestUser.getAnnotation() == null
                ? eventEntity.getAnnotation() : eventUpdateRequestUser.getAnnotation());

        if (eventUpdateRequestUser.getCategory() != null) {
            CategoryEntity categoryEntity = catRepository.findById(eventUpdateRequestUser.getCategory())
                    .orElseThrow(() -> new NotFoundException("Categories with id=" + eventUpdateRequestUser.getCategory() + " not found"));
            eventEntity.setCategory(eventUpdateRequestUser.getCategory() == null
                    ? eventEntity.getCategory() : categoryEntity);
        }

        eventEntity.setDescription(eventUpdateRequestUser.getDescription() == null
                ? eventEntity.getDescription() : eventUpdateRequestUser.getDescription());

        eventEntity.setEventDate(eventUpdateRequestUser.getEventDate() == null
                ? eventEntity.getEventDate() : eventUpdateRequestUser.getEventDate());

        if (eventUpdateRequestUser.getLocation() != null) {
            float lat = eventUpdateRequestUser.getLocation().getLat();
            float lon = eventUpdateRequestUser.getLocation().getLon();
            eventEntity.setLat(lat);
            eventEntity.setLon(lon);
        }

        if (eventUpdateRequestUser.getPaid() != null) {
            eventEntity.setPaid(eventUpdateRequestUser.getPaid());
        }

        eventEntity.setParticipantLimit(eventUpdateRequestUser.getParticipantLimit() == null
                ? eventEntity.getParticipantLimit() : eventUpdateRequestUser.getParticipantLimit());

        if (eventUpdateRequestUser.getRequestModeration() != null) {
            eventEntity.setRequestModeration(eventUpdateRequestUser.getRequestModeration());
        }

        eventEntity.setTitle(eventUpdateRequestUser.getTitle() == null
                ? eventEntity.getTitle() : eventUpdateRequestUser.getTitle());

        if (eventUpdateRequestUser.getStateAction() != null) {
            if (eventUpdateRequestUser.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                eventEntity.setState(States.CANCELED);
            } else {
                eventEntity.setState(States.PENDING);
            }
        }

        return eventMapper.toEventResponse(eventEntity);
    }

    @Override
    public List<EventResponse> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               Sort sort, int from, int size, HttpServletRequest httpServletRequest) {


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = criteriaBuilder.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (text != null && !text.isEmpty()) {
            String searchPattern = "%" + text.toLowerCase() + "%";
            Predicate annotationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), searchPattern);
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);
            predicates.add(criteriaBuilder.or(annotationPredicate, descriptionPredicate));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categories));
        }

        if (paid != null) {
            predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
        }


        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new DateException("invalid date");
            }
            predicates.add(criteriaBuilder.between(root.get("eventDate"), rangeStart, rangeEnd));
        } else {
            predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }

        if (Boolean.TRUE.equals(onlyAvailable)) {
            predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(root.get("participantLimit")),
                    criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))));
        }

        query.select(root).where(predicates.toArray(new Predicate[0]));

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE -> query.orderBy(criteriaBuilder.asc(root.get("eventDate")));
                case VIEWS -> query.orderBy(criteriaBuilder.asc(root.get("views")));
                case RATING -> query.orderBy(criteriaBuilder.desc(root.get("rating")));
                default -> throw new DateException("sort is not supported");
            }
        }

        TypedQuery<EventEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        List<EventEntity> eventEntities = typedQuery.getResultList();

        sendStatList(eventEntities, httpServletRequest);
        setViewList(eventEntities, httpServletRequest);
        return typedQuery.getResultList().stream().map(eventMapper::toEventResponse).toList();
    }

    @Override
    public EventResponse getEventById(long eventId, HttpServletRequest httpServletRequest) {
        EventEntity eventEntity = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!eventEntity.getState().equals(States.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        sendStat(httpServletRequest);
        setView(eventEntity, httpServletRequest);
        return eventMapper.toEventResponse(eventEntity);
    }

    private void validateUserId(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private void sendStatList(List<EventEntity> eventEntities, HttpServletRequest httpServletRequest) {

        HitDto hitDto = new HitDto();
        hitDto.setApp("ewm-main-service");
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now().format(dateTimeFormatter));
        for (EventEntity event : eventEntities) {
            hitDto.setUri(httpServletRequest.getRequestURI() + "/" + event.getId());
            statsClient.addHit(hitDto);
        }
        hitDto.setUri(httpServletRequest.getRequestURI());
        statsClient.addHit(hitDto);
    }

    private void sendStat(HttpServletRequest httpServletRequest) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("ewm-main-service");
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now().format(dateTimeFormatter));
        statsClient.addHit(hitDto);

    }

    private void setViewList(List<EventEntity> eventEntities, HttpServletRequest httpServletRequest) {
        String start = eventEntities.stream().min(Comparator.comparing(EventEntity::getCreatedOn)).orElseThrow().getCreatedOn().format(dateTimeFormatter);
        String end = LocalDateTime.now().format(dateTimeFormatter);

        List<String> uris = eventEntities
                .stream()
                .map(eventEntity -> httpServletRequest.getRequestURI() + "/" + eventEntity.getId())
                .toList();

        List<StatDto> statDtos = statsClient.getStats(
                start,
                end,
                uris,
                true);

        Map<String, Long> viewMap = statDtos.stream().collect(Collectors.toMap(StatDto::getUri, StatDto::getHits));

        for (EventEntity eventEntity : eventEntities) {
            String uri = httpServletRequest.getRequestURI() + "/" + eventEntity.getId();
            Long views = viewMap.getOrDefault(uri, 0L);
            eventEntity.setViews(views);
        }
    }

    private void setView(EventEntity eventEntities, HttpServletRequest httpServletRequest) {
        String start = eventEntities.getCreatedOn().format(dateTimeFormatter);
        String end = LocalDateTime.now().format(dateTimeFormatter);

        String uris = httpServletRequest.getRequestURI();


        List<StatDto> statDtos = statsClient.getStats(
                start,
                end,
                Arrays.asList(uris),
                true);

        eventEntities.setViews(statDtos.getFirst().getHits());
    }
}
