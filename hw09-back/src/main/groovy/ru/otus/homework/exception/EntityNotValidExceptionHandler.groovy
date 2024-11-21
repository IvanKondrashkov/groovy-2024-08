package ru.otus.homework.exception

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.http.annotation.Produces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor

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
                .build(), HttpResponse.badRequest()
        )
    }
}