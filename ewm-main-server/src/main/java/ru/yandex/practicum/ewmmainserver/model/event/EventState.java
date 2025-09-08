package ru.yandex.practicum.ewmmainserver.model.event;

import ru.yandex.practicum.ewmmainserver.exception.InvalidInputException;

public enum EventState {
    PUBLISHED,
    PENDING,
    CANCELED;

    public static EventState fromString(String stateStr) {
        try {
            return EventState.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Нет такого статуса: " + stateStr);
        }
    }
}
