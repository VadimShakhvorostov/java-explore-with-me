package ru.practicum.main.service.ratings;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.ratings.RatingResponseDto;
import ru.practicum.main.dto.ratings.mapper.RatingMapper;
import ru.practicum.main.dto.users.UserPrivateResponse;
import ru.practicum.main.dto.users.mapper.UserMapper;
import ru.practicum.main.enums.States;
import ru.practicum.main.enums.VoteType;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.RequestException;
import ru.practicum.main.repositories.events.EventEntity;
import ru.practicum.main.repositories.events.EventsRepository;
import ru.practicum.main.repositories.rating.RatingEntity;
import ru.practicum.main.repositories.rating.RatingRepository;
import ru.practicum.main.repositories.users.UserEntity;
import ru.practicum.main.repositories.users.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final RatingMapper ratingMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public RatingResponseDto addVote(Long userId, Long eventId, VoteType voteType) {
        EventEntity eventEntity = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!eventEntity.getState().equals(States.PUBLISHED)) {
            throw new RequestException("Event is not published");
        }

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        if (eventEntity.getInitiator().equals(userEntity)) {
            throw new RequestException("You cannot add vote to yourself");
        }

        RatingEntity newRating = new RatingEntity();
        Optional<RatingEntity> ratingInDB = ratingRepository.findByUserIdAndEventId(userId, eventId);

        if (ratingInDB.isPresent()) {
            RatingEntity ratingToUpdate = ratingInDB.get();

            if (ratingToUpdate.getType().equals(voteType)) {
                return ratingMapper.toRatingResponseDto(ratingToUpdate);
            }

            if (voteType.equals(VoteType.LIKE) && ratingToUpdate.getType().equals(VoteType.DISLIKE)) {
                eventEntity.setLikeCount(eventEntity.getLikeCount() + 1);
                eventEntity.setDislikeCount(eventEntity.getDislikeCount() - 1);
            } else if (voteType.equals(VoteType.DISLIKE) && ratingToUpdate.getType().equals(VoteType.LIKE)) {
                eventEntity.setLikeCount(eventEntity.getLikeCount() - 1);
                eventEntity.setDislikeCount(eventEntity.getDislikeCount() + 1);
            }

            ratingToUpdate.setType(voteType);
            ratingToUpdate.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            ratingRepository.save(ratingToUpdate);

        } else {

            newRating.setEvent(eventEntity);
            newRating.setUser(userEntity);
            newRating.setType(voteType);
            newRating.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            ratingRepository.save(newRating);

            if (voteType.equals(VoteType.LIKE)) {
                eventEntity.setLikeCount(eventEntity.getLikeCount() + 1);
            } else {
                eventEntity.setDislikeCount(eventEntity.getDislikeCount() + 1);
            }
        }

        float rating = (eventEntity.getDislikeCount() + eventEntity.getLikeCount()) == 0
                ? 0
                : (float) eventEntity.getLikeCount() / (eventEntity.getDislikeCount() + eventEntity.getLikeCount());
        eventEntity.setRating(rating);

        eventsRepository.save(eventEntity);
        return ratingMapper.toRatingResponseDto(ratingInDB.orElse(newRating));
    }

    @Override
    public List<UserPrivateResponse> getVotedUsers(Long userId, Long eventId, VoteType voteType, int from, int size) {
        EventEntity eventEntity = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        if (!eventEntity.getInitiator().equals(userEntity)) {
            throw new RequestException("You cannot vote this user");
        }

        List<UserEntity> users = ratingRepository.getRatingEntitiesByEventId(eventId, voteType, PageRequest.of(from, size))
                .stream()
                .map(RatingEntity::getUser)
                .toList();
        return users.stream().map(userMapper::toUserPrivateResponse).toList();

    }
}
