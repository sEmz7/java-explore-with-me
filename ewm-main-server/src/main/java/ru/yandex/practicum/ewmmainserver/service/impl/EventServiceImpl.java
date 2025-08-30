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
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getAllByUser(long userId, int from, int size) {
        findUserByIdOrThrow(userId);
        Pageable pageable = PageRequest.of(0, from + size, Sort.by("id"));

        List<EventEntity> events = eventRepository.findAllByInitiatorId(userId, pageable);

        return events.stream()
                .skip(from)
                .limit(size)
                .map(eventMapper::toDto)
                .toList();
    }

    @Override
    public EventFullDto create(NewEventDto dto, long userId) {
        UserEntity user = findUserByIdOrThrow(userId);
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

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getById(long userId, long eventId) {
        UserEntity user = findUserByIdOrThrow(userId);
        EventEntity event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Нет события с id={}", eventId);
            return new NotFoundException("Нет события с id=" + eventId);
        });
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Просмотреть событие может только его владелец");
        }
        return eventMapper.toDto(event);
    }

    private UserEntity findUserByIdOrThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Нет пользователя с id={}", userId);
            return new NotFoundException("Нет пользователя с id=" + userId);
        });
    }
}
