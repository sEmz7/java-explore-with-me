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
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.model.event.EventState;
import ru.yandex.practicum.ewmmainserver.model.eventComment.EventCommentEntity;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentRequestDto;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentResponseDto;
import ru.yandex.practicum.ewmmainserver.model.eventComment.mapper.EventCommentMapper;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.repository.EventCommentRepository;
import ru.yandex.practicum.ewmmainserver.repository.EventRepository;
import ru.yandex.practicum.ewmmainserver.repository.UserRepository;
import ru.yandex.practicum.ewmmainserver.service.EventCommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventCommentServiceImpl implements EventCommentService {
    private final EventCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventCommentMapper commentMapper;

    @Override
    public EventCommentResponseDto create(long userId, long eventId, EventCommentRequestDto dto) {
        UserEntity user = findOrThrowUserById(userId);
        EventEntity event = findOrThrowEventById(eventId);
        if (commentRepository.findByEventIdAndUserId(eventId, userId).isPresent()) {
            throw new ConflictException("Вы можете оставить только один комментарий к событию.");
        }
        EventCommentEntity comment = commentMapper.toEntity(dto);
        comment.setUser(user);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        comment.setStatus(EventState.PENDING);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public EventCommentResponseDto update(long userId, long eventId, long commId, EventCommentRequestDto dto) {
        findOrThrowUserById(userId);
        findOrThrowEventById(eventId);
        EventCommentEntity comment = findOrThrowCommentById(commId);
        comment.setText(dto.getText());
        if (comment.getStatus() == EventState.CANCELED) {
            comment.setStatus(EventState.PENDING);
        }
        return commentMapper.toDto(comment);
    }

    @Override
    public void delete(long userId, long eventId, long commId) {
        UserEntity user = findOrThrowUserById(userId);
        findOrThrowEventById(eventId);
        EventCommentEntity comment = findOrThrowCommentById(commId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ConflictException("Удалить комментарий может только его владелец.");
        }
        commentRepository.deleteById(commId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventCommentResponseDto> getCommentsByEvent(long userId, long eventId, int from, int size) {
        findOrThrowUserById(userId);
        findOrThrowEventById(eventId);
        Pageable pageable = PageRequest.of(0, from + size,
                Sort.by(Sort.Direction.DESC, "createdOn"));
        List<EventCommentEntity> comments =
                commentRepository.findAllByEventIdAndStatus(eventId, EventState.PUBLISHED, pageable).getContent();
        return comments.stream()
                .skip(from)
                .limit(size)
                .map(commentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public EventCommentResponseDto getById(long userId, long eventId, long commId) {
        findOrThrowUserById(userId);
        findOrThrowEventById(eventId);
        return commentMapper.toDto(commentRepository.findByIdAndStatus(commId, EventState.PUBLISHED));
    }

    @Override
    public EventCommentResponseDto updateStatus(long commId, String status) {
        EventCommentEntity comment = findOrThrowCommentById(commId);
        EventState state = EventState.fromString(status);
        comment.setStatus(state);
        return commentMapper.toDto(comment);
    }

    private UserEntity findOrThrowUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id={} не найден", userId);
            return new NotFoundException("Пользователь с id=" + userId + " не найден");
        });
    }

    private EventEntity findOrThrowEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Событие с id={} не найдено", eventId);
            return new NotFoundException("Событие с id=" + eventId + " не найдено");
        });
    }

    private EventCommentEntity findOrThrowCommentById(long commId) {
        return commentRepository.findById(commId).orElseThrow(() -> {
            log.warn("Комментария с id={} не найдено", commId);
            return new NotFoundException("Комментария с id=" + commId + " не найдено");
        });
    }
}
