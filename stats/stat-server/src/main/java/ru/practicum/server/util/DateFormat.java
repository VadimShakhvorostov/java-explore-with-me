package ru.practicum.server.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateFormat {
    public static LocalDateTime toLocaleDateTime(String dateString) {
        dateString = dateString.replace("%20", " ").replace(" ", "T");
        if (!dateString.contains("T")) {
            dateString += "T00:00:00";
        }
        if (dateString.contains(".")) {
            dateString = dateString.split("\\.")[0];
        }
        try {
            return LocalDateTime.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format " + dateString);
        }
    }
}
