package ru.otus.homework.exception

class EntityNotValidException extends RuntimeException {
    EntityNotValidException(String message, Throwable cause) {
        super(message, cause)
    }

    EntityNotValidException(String message) {
        super(message)
    }
}