package ru.yandex.practicum.ewmmainserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainserver.exception.ConflictException;
import ru.yandex.practicum.ewmmainserver.exception.NotFoundException;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;
import ru.yandex.practicum.ewmmainserver.model.event.mapper.EventMapper;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.repository.CategoryRepository;
import ru.yandex.practicum.ewmmainserver.repository.EventRepository;
import ru.yandex.practicum.ewmmainserver.repository.UserRepository;
import ru.yandex.practicum.ewmmainserver.service.EventService;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto create(NewEventDto dto, long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Нет пользователя с id={}", userId);
            return new NotFoundException("Нет пользователя с id=" + userId);
        });
        EventEntity event = eventMapper.toEntity(dto);
        CategoryEntity category = categoryRepository.findById(dto.getCategory()).orElseThrow(() -> {
            log.warn("Нет категории c id={}", dto.getCategory());
            return new NotFoundException("Нет категории с id=" + dto.getCategory());
        });
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата события должна быть не ранее чем через 2 часа от текущего момента");
        }
        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        EventEntity savedEvent = eventRepository.save(event);
        log.debug("Сохранено событие с id={}", event.getId());
        return eventMapper.toDto(savedEvent);
    }
}
