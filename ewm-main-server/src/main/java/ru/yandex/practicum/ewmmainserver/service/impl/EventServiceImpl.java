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
import ru.yandex.practicum.ewmmainserver.model.event.EventState;
import ru.yandex.practicum.ewmmainserver.model.event.EventStateAction;
import ru.yandex.practicum.ewmmainserver.model.event.SortTypes;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventShortDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.UpdateEventDto;
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
        CategoryEntity category = findCategoryByIdOrThrow(dto.getCategory());
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата события должна быть не ранее чем через 2 часа от текущего момента");
        }
        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setViews(0);
        EventEntity savedEvent = eventRepository.save(event);
        log.debug("Сохранено событие с id={}", event.getId());
        return eventMapper.toDto(savedEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getById(long userId, long eventId) {
        UserEntity user = findUserByIdOrThrow(userId);
        EventEntity event = findEventByIdOrThrow(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Просмотреть событие может только его владелец");
        }
        return eventMapper.toDto(event);
    }

    @Override
    public EventFullDto update(UpdateEventDto dto, long userId, long eventId) {
        EventEntity event = findEventByIdOrThrow(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Редактировать событие может только его владелец");
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя отредактировать опубликованное событие");
        }
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException(
                    "Дата и время события должны быть не ранее чем через 2 часа от текущего момента"
            );
        }
        if (dto.getCategory() != null) {
            CategoryEntity category = findCategoryByIdOrThrow(dto.getCategory());
            event.setCategory(category);
        }
        if (dto.getStateAction() != null && dto.getStateAction().equals(EventStateAction.CANCEL_REVIEW.toString())) {
            event.setState(EventState.CANCELED);
        }
        eventMapper.updateEventFromDto(dto, event);
        return eventMapper.toDto(event);
    }

    @Override
    public EventFullDto editStatus(UpdateEventDto dto, long eventId) {
        EventEntity event = findEventByIdOrThrow(eventId);
        if (dto.getStateAction() != null) {
            EventStateAction action = EventStateAction.valueOf(dto.getStateAction());
            switch (action) {
                case PUBLISH_EVENT ->  publishEvent(event);
                case REJECT_EVENT -> rejectEvent(event);
            }
        }
        if (event.getPublishedOn() != null && dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
                throw new ConflictException(
                        "Дата начала изменяемого события должна быть не ранее чем за час от даты публикации"
                );
            }
        }
        eventMapper.updateEventFromDto(dto, event);
        return eventMapper.toDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(0, from + size, Sort.by("id"));
        return eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, pageable)
                .getContent()
                .stream()
                .skip(from)
                .limit(size)
                .map(eventMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Boolean onlyAvailable, SortTypes sort, int from, int size) {
        Pageable pageable = sort == SortTypes.EVENT_DATE
                ? PageRequest.of(0, from + size, Sort.by("eventDate"))
                : PageRequest.of(0, from + size, Sort.by("views"));
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        List<EventEntity> events = eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable).getContent();
        return events.stream()
                .skip(from)
                .limit(size)
                .map(eventMapper::toShortDto)
                .toList();
    }

    private void publishEvent(EventEntity event) {
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Событие можно опубликовать, только если оно в состоянии ожидания публикации");
        }
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
    }

    private void rejectEvent(EventEntity event) {
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Cобытие можно отменить, только если оно еще не опубликовано");
        }
        event.setState(EventState.CANCELED);
        event.setPublishedOn(null);
    }

    private UserEntity findUserByIdOrThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Нет пользователя с id={}", userId);
            return new NotFoundException("Нет пользователя с id=" + userId);
        });
    }

    private EventEntity findEventByIdOrThrow(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Нет события с id={}", eventId);
            return new NotFoundException("Нет события с id=" + eventId);
        });
    }

    private CategoryEntity findCategoryByIdOrThrow(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            log.warn("Нет категории c id={}", catId);
            return new NotFoundException("Нет категории с id=" + catId);
        });
    }

}
