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
import ru.otus.homework.exception.EntityNotFoundException

@Produces
@Singleton
@Requires(classes = [EntityNotFoundException.class, ExceptionHandler.class])
class EntityNotFoundExceptionHandler implements ExceptionHandler<EntityNotFoundException, HttpResponse> {
    @Inject
    ErrorResponseProcessor<?> errorResponseProcessor

    @Override
    HttpResponse handle(HttpRequest request, EntityNotFoundException exception) {
        return errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(exception)
                .errorMessage('Entity not found!')
                .build(), HttpResponseFactory.INSTANCE.status(HttpStatus.NOT_FOUND)
        )
    }
}