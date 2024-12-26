package ru.practicum.main.dto.ratings.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.dto.ratings.RatingResponseDto;
import ru.practicum.main.repositories.rating.RatingEntity;

@Component
public class RatingMapper {
    public RatingResponseDto toRatingResponseDto(RatingEntity ratingEntity) {
        RatingResponseDto ratingResponseDto = new RatingResponseDto();
        ratingResponseDto.setId(ratingEntity.getId());
        ratingResponseDto.setUser(ratingEntity.getUser().getId());
        ratingResponseDto.setEvent(ratingEntity.getEvent().getId());
        ratingResponseDto.setVoteType(ratingEntity.getType());
        ratingResponseDto.setCreatedAt(ratingEntity.getCreatedOn());
        return ratingResponseDto;
    }
}
