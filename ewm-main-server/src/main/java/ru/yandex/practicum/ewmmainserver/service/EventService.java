package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;

public interface EventService {
    EventFullDto create(NewEventDto dto, long userId);
}
