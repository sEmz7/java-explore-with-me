package ru.yandex.practicum.ewmmainserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainserver.exception.ConflictException;
import ru.yandex.practicum.ewmmainserver.exception.NotFoundException;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.model.event.EventState;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.RequestEntity;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.RequestStatus;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.RequestDto;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.repository.EventRepository;
import ru.yandex.practicum.ewmmainserver.repository.RequestRepository;
import ru.yandex.practicum.ewmmainserver.repository.UserRepository;
import ru.yandex.practicum.ewmmainserver.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getUserRequests(long userId) {
        findUserByIdOrThrow(userId);
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    public RequestDto createRequest(long userId, long eventId) {
        UserEntity user = findUserByIdOrThrow(userId);
        EventEntity event = findEventByIdOrThrow(eventId);
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() != 0
                && requestRepository.findAllByEventId(eventId).size() == event.getParticipantLimit()) {
            throw new ConflictException("У события достигнут лимит запросов на участие");
        }
        RequestEntity request = new RequestEntity();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(!event.getRequestModeration() || event.getParticipantLimit() == 0
                ? RequestStatus.CONFIRMED
                : RequestStatus.PENDING);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public RequestDto cancelUserRequest(long userId, long requestId) {
        findUserByIdOrThrow(userId);
        RequestEntity request = requestRepository.findById(requestId).orElseThrow(() -> {
           log.warn("Не найден запрос на участие с id={}", requestId);
           return new NotFoundException("Не найден щапрос на участие с id=" + requestId);
        });
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toDto(request);
    }

    private UserEntity findUserByIdOrThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id={} не найден", userId);
            return new NotFoundException("Пользователь с id=" + userId + " не найден.");
        });
    }

    private EventEntity findEventByIdOrThrow(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Событие с id={} не найдено", eventId);
            return new NotFoundException("Событие с id=" + eventId + " не найдено.");
        });
    }
}
