package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentRequestDto;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentResponseDto;

import java.util.List;

public interface EventCommentService {
    EventCommentResponseDto create(long userId, long eventId, EventCommentRequestDto dto);

    EventCommentResponseDto update(long userId, long eventId, long commId, EventCommentRequestDto dto);

    void delete(long userId, long eventId, long commId);

    List<EventCommentResponseDto> getCommentsByEvent(long userId, long eventId, int from, int size);

    EventCommentResponseDto getById(long userId, long eventId, long commId);

    EventCommentResponseDto updateStatus(long commId, String status);
}
