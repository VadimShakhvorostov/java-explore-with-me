package ru.practicum.main.dto.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.dto.request.RequestDtoResponse;
import ru.practicum.main.repositories.request.RequestEntity;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDtoResponse toRequestDto(RequestEntity requestEntity);
}
