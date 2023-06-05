package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class HandlerException {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundError(final RuntimeException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse("Что-то не найдено", e.getMessage());
    }

    @ExceptionHandler(EmailDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse duplicate(final RuntimeException e) {
        log.debug("Получен статус 409 Not found {}", e.getMessage(), e);
        return new ErrorResponse("Дубли вне закона", e.getMessage());
    }

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(final MissingRequestHeaderException e) {
        log.debug("Получен статус 400 Not found {}", e.getMessage(), e);
        return new ErrorResponse("Повнимательней плиз", e.getMessage());
    }
}

