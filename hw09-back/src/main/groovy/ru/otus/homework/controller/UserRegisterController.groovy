package ru.otus.homework.controller

import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import jakarta.validation.Valid
import groovy.util.logging.Slf4j
import io.micronaut.validation.Validated
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.http.annotation.*
import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo
import ru.otus.homework.service.UserRegisterService

@Slf4j
@Validated
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/users/register")
class UserRegisterController {
    @Inject
    UserRegisterService userService

    @Status(HttpStatus.CREATED)
    @Post(produces="application/json")
    UserInfo save(@Body @Valid User user) {
        log.info("Send save request /users/register")
        return userService.save(user)
    }
}