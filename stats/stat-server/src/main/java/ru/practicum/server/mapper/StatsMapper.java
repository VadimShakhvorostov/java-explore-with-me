package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.StatDto;
import ru.practicum.server.entity.StatEntity;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    StatDto toStatDto(StatEntity statEntity);

    StatEntity toStatEntity(StatDto statDto);

}
