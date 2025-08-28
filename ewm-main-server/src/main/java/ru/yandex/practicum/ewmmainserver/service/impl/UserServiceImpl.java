package ru.yandex.practicum.ewmmainserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainserver.exception.ConflictException;
import ru.yandex.practicum.ewmmainserver.exception.NotFoundException;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserRequestDto;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserResponseDto;
import ru.yandex.practicum.ewmmainserver.model.user.mapper.UserMapper;
import ru.yandex.practicum.ewmmainserver.repository.UserRepository;
import ru.yandex.practicum.ewmmainserver.service.UserService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers(List<Long> ids, int from, int size) {
        if (ids != null) {
            return userRepository.findByIdIn(ids)
                    .stream()
                    .map(userMapper::toDto)
                    .toList();
        }
        Pageable pageable = PageRequest.of(0, from + size, Sort.by("id"));

        List<UserEntity> users = userRepository.findAll(pageable).getContent();

        return users.stream()
                .skip(from)
                .limit(size)
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto create(UserRequestDto dto) {
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с email={" + dto.getEmail() + "} уже зарегистрирован.");
        }
        UserEntity userEntity = userMapper.toEntity(dto);
        UserEntity savedUser = userRepository.save(userEntity);
        log.debug("Создан пользователь с id={}, name={}, email={}",
                savedUser.getId(), savedUser.getName(),savedUser.getEmail());
        return userMapper.toDto(savedUser);
    }

    @Override
    public void delete(long userId) {
        findUserByIdOrThrow(userId);
        userRepository.deleteById(userId);
        log.debug("Удален пользователь с id={}.", userId);
    }

    private UserEntity findUserByIdOrThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id={} не найден.", userId);
            return new NotFoundException("Пользователь с id=" + userId + " не найден.");
        });
    }
}
