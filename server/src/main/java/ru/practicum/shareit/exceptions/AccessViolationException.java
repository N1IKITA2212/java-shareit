package ru.practicum.shareit.exceptions;

public class AccessViolationException extends RuntimeException {
    public AccessViolationException(String message) {
        super(message);
    }
}
