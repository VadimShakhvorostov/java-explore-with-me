package ru.practicum.main.dto.users.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.dto.users.UserPrivateResponse;
import ru.practicum.main.dto.users.UserRequest;
import ru.practicum.main.dto.users.UserResponse;
import ru.practicum.main.repositories.users.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toUserEntity(UserRequest userRequest);

    UserResponse toUserResponse(UserEntity userEntity);

    UserPrivateResponse toUserPrivateResponse(UserEntity userEntity);
}
