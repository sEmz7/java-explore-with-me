package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.user.dto.UserRequestDto;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers(List<Long> ids, int from, int size);

    UserResponseDto create(UserRequestDto dto);

    void delete(long userId);
}
