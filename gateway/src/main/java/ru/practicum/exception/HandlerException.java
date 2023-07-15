package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class HandlerException {

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(final Exception e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse("Повнимательней плиз", e.getMessage());
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse statusError(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerError(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return new ErrorResponse("что-то сломалось", e.getMessage());
    }

}

