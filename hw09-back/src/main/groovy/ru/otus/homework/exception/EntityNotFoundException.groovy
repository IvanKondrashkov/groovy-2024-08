package ru.otus.homework.exception

class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException(String message, Throwable cause) {
        super(message, cause)
    }

    EntityNotFoundException(String message) {
        super(message)
    }
}