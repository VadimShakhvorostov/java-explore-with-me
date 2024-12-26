package ru.practicum.main.dto.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.enums.VoteType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequestDto {

    private Long userId;
    private Long eventId;
    private VoteType voteType;
}
