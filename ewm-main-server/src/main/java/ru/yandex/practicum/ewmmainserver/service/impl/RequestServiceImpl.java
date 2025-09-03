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
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.RequestDto;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.repository.EventRepository;
import ru.yandex.practicum.ewmmainserver.repository.RequestRepository;
import ru.yandex.practicum.ewmmainserver.repository.UserRepository;
import ru.yandex.practicum.ewmmainserver.service.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getEventRequests(long userId, long eventId) {
        findUserByIdOrThrow(userId);
        findEventByIdOrThrow(eventId);
        List<RequestEntity> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsStatuses(long userId, long eventId,
                                                                 EventRequestStatusUpdateRequest dto) {
        EventEntity event = findEventByIdOrThrow(eventId);
        findUserByIdOrThrow(userId);
        List<RequestEntity> inputRequests = requestRepository.findAllByIdIn(dto.getRequestIds());
        return switch (dto.getStatus()) {
            case CONFIRMED -> confirmRequests(inputRequests, event);
            case PENDING, CANCELED -> new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
            case REJECTED -> cancelRequests(inputRequests);
        };
    }

    private EventRequestStatusUpdateResult confirmRequests(List<RequestEntity> requests,
                                                           EventEntity event) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            List<RequestDto> requestDtos = requests.stream()
                    .map(requestMapper::toDto)
                    .toList();
            return new EventRequestStatusUpdateResult(requestDtos, new ArrayList<>());
        }
        int eventConfirmedRequests = event.getConfirmedRequests();
        if (event.getParticipantLimit() == eventConfirmedRequests) {
            throw new ConflictException(
                    "Нельзя подтвердить заявку, так как уже достигнут лимит по заявкам на данное событие"
            );
        }
        List<RequestDto> confirmed = new ArrayList<>();
        List<RequestDto> rejected = new ArrayList<>();
        for (RequestEntity request : requests) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException(
                        "Статус можно изменить только у заявок, находящихся в состоянии ожидания"
                );
            }
            if (event.getParticipantLimit() == eventConfirmedRequests) {
                request.setStatus(RequestStatus.REJECTED);
                rejected.add(requestMapper.toDto(request));
            } else {
                request.setStatus(RequestStatus.CONFIRMED);
                eventConfirmedRequests++;
                confirmed.add(requestMapper.toDto(request));
            }
        }
        event.setConfirmedRequests(confirmed.size());
        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    private EventRequestStatusUpdateResult cancelRequests(List<RequestEntity> requests) {
        requests
                .forEach(request -> {
                    if (request.getStatus() != RequestStatus.PENDING) {
                        throw new ConflictException(
                                "Статус можно изменить только у заявок, находящихся в состоянии ожидания"
                        );
                    }
                    request.setStatus(RequestStatus.REJECTED);
                });
        List<RequestDto> dtos = requests
                .stream()
                .map(requestMapper::toDto)
                .toList();
        return new EventRequestStatusUpdateResult(new ArrayList<>(), dtos);
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
