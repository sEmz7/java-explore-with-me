package ru.yandex.practicum.ewmmainserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice({"ru.yandex.practicum.ewmmainserver"})
public class ErrorHandler {
    private static final String BAD_REQUEST_REASON = "Incorrectly made request.";
    private static final String CONFLICT_REASON = "Integrity constraint has been violated.";
    private static final String NOT_FOUND_REASON = "The required object was not found.";
    private static final String SERVER_ERROR_REASON = "Server error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final InvalidInputException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_REASON, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final ConflictException e) {
        return new ErrorResponse(HttpStatus.CONFLICT, CONFLICT_REASON, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, NOT_FOUND_REASON, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_REASON, message, LocalDateTime.now());
    }

    @ExceptionHandler
    public ErrorResponse handleHttpMessageNotReadable(final HttpMessageNotReadableException e) {
        String message = e.getLocalizedMessage();
        return new ErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_REASON, message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameter(final MissingServletRequestParameterException e) {
        String message = String.format("Отсутствует обязательный параметр: %s", e.getParameterName());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_REASON, message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidation(final HandlerMethodValidationException e) {
        String message = e.getAllValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream()
                .map(error -> r.getMethodParameter().getParameterName() + ": " +
                        error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                BAD_REQUEST_REASON,
                message,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerError(final Throwable e) {
        String message = e.getLocalizedMessage();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, SERVER_ERROR_REASON, message, LocalDateTime.now());
    }
}
