package ru.practicum.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(final NotFoundException ex) {
        return new ExceptionResponse(
                ex.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse requestException(final RequestException ex) {
        return new ExceptionResponse(
                ex.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse requestException(final DateException ex) {
        return new ExceptionResponse(
                ex.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString());
    }


}
