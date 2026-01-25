package ru.practicum.shareit.exceptions;

public class AlreadyExistsUserException extends RuntimeException {
    public AlreadyExistsUserException(String message) {
        super(message);
    }
}
