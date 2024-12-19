package ru.practicum.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;
}