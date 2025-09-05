package ru.yandex.practicum.ewmmainserver.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
