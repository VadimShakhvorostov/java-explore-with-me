package ru.practicum.main.dto.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.enums.VoteType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponseDto {

    private long id;
    private long user;
    private long event;
    private VoteType voteType;
    private LocalDateTime createdAt;

}

