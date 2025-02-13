package ru.practicum.main.service.users;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.users.UserRequest;
import ru.practicum.main.dto.users.UserResponse;
import ru.practicum.main.dto.users.mapper.UserMapper;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.repositories.users.UserEntity;
import ru.practicum.main.repositories.users.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getUsers(List<Long> ids, int from, int size) {
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(PageRequest.of(from, size)).map(userMapper::toUserResponse).toList();
        } else {
            return userRepository.getUsersById(ids, PageRequest.of(from, size))
                    .stream()
                    .map(userMapper::toUserResponse)
                    .toList();
        }
    }

    @Override
    @Transactional
    public UserResponse addUser(UserRequest userRequest) {
        UserEntity userEntity = userRepository.save(userMapper.toUserEntity(userRequest));
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.trace("deleteUser, userId = {}", userId);
        validateUserId(userId);
        userRepository.deleteById(userId);
    }

    private void validateUserId(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

    }
}
