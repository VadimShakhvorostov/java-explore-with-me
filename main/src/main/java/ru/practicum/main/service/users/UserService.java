package ru.practicum.main.service.users;

import ru.practicum.main.dto.users.UserRequest;
import ru.practicum.main.dto.users.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getUsers(List<Long> ids, int from, int size);

    UserResponse addUser(UserRequest userRequest);

    void deleteUser(long userId);


}
