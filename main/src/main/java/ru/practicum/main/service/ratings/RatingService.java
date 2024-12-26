package ru.practicum.main.service.ratings;

import ru.practicum.main.dto.ratings.RatingResponseDto;
import ru.practicum.main.dto.users.UserPrivateResponse;
import ru.practicum.main.enums.VoteType;

import java.util.List;

public interface RatingService {

    RatingResponseDto addVote(Long userId, Long eventId, VoteType voteType);

    List<UserPrivateResponse> getVotedUsers(Long userId, Long eventId, VoteType voteType, int from, int size);

}
