package ru.practicum.main.controller.priv;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.ratings.RatingResponseDto;
import ru.practicum.main.dto.users.UserPrivateResponse;
import ru.practicum.main.enums.VoteType;
import ru.practicum.main.service.ratings.RatingServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/ratings/{userId}/vote/{eventId}")
@AllArgsConstructor
public class PrivateRatingController {

    private final RatingServiceImpl ratingServiceImpl;

    @PostMapping
    public RatingResponseDto addVote(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestParam VoteType voteType) {
        return ratingServiceImpl.addVote(userId, eventId, voteType);
    }

    @GetMapping
    public List<UserPrivateResponse> getVotedUsers(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestParam VoteType voteType,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        return ratingServiceImpl.getVotedUsers(userId, eventId, voteType, from, size);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVote(@PathVariable Long userId, @PathVariable Long eventId) {
        ratingServiceImpl.deleteVote(userId, eventId);
    }
}
