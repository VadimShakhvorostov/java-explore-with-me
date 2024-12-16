package ru.practicum.main.dto.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.dto.compilation.CompilationResponse;
import ru.practicum.main.repositories.compilation.CompilationEntity;

@Mapper(componentModel = "spring")
public interface CompilationsMapper {

    CompilationResponse toCompilationResponse(CompilationEntity compilationEntity);

}
