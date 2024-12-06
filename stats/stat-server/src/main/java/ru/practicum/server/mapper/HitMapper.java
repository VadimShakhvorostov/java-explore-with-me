package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.HitDto;
import ru.practicum.server.entity.HitEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface HitMapper {

    HitEntity toHitEntity(HitDto hitDto);

    HitDto toHitDto(HitEntity hitEntity);

    default LocalDateTime map(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timestamp, formatter);
    }
}
