package ru.otus.homework.exception.handlers

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpResponseFactory
import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.http.annotation.Produces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import ru.otus.homework.exception.EntityNotValidException

@Produces
@Singleton
@Requires(classes = [EntityNotValidException.class, ExceptionHandler.class])
class EntityNotValidExceptionHandler implements ExceptionHandler<EntityNotValidException, HttpResponse> {
    @Inject
    ErrorResponseProcessor<?> errorResponseProcessor

    @Override
    HttpResponse handle(HttpRequest request, EntityNotValidException exception) {
        return errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(exception)
                .errorMessage('Entity not valid!')
                .build(), HttpResponseFactory.INSTANCE.status(HttpStatus.CONFLICT)
        )
    }
}