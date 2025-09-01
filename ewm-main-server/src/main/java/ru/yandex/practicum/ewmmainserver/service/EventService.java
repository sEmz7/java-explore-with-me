package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.UpdateEventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getAllByUser(long userId, int from, int size);

    EventFullDto create(NewEventDto dto, long userId);

    EventFullDto getById(long userId, long eventId);

    EventFullDto update(UpdateEventDto dto, long userId, long eventId);

    EventFullDto editStatus(UpdateEventDto dto, long eventId);

    List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

}
