package ru.practicum.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse notFound(final DateTimeException ex) {
        return new ExceptionResponse(
                ex.getMessage(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString());
    }
}

