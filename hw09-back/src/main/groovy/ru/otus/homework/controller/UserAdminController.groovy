package ru.otus.homework.controller

import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import jakarta.validation.Valid
import groovy.util.logging.Slf4j
import jakarta.annotation.Nullable
import io.micronaut.validation.Validated
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.http.annotation.*
import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.service.UserAdminService

@Slf4j
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/users/admin")
class UserAdminController {
    @Inject
    UserAdminService userService

    @Put(uri="/{id}", produces="application/json")
    UserInfo updateById(@Body @Valid User user, @PathVariable UUID id) {
        log.info("Send update request /users/admin/{}", id)
        return userService.updateById(user, id)
    }

    @Get(uri="/{id}", produces="application/json")
    UserInfo findById(@PathVariable UUID id) {
        log.info("Send get request /users/admin/{}", id)
        return userService.findById(id)
    }

    @Get(produces="application/json")
    List<UserInfo> findAll(Pageable pageable) {
        log.info("Send get request /users/admin?page={}&size={}&sort={}", pageable.number, pageable.size, pageable.sort)
        return userService.findAll(pageable)
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@PathVariable UUID id) {
        log.info("Send delete request /users/admin/{}", id)
        userService.deleteById(id)
    }

    @Get(uri="/verify{?login,password}", produces="application/json")
    UserInfo findByLoginAndPassword(@QueryValue @Nullable String login, @QueryValue @Nullable String password) {
        log.info("Send get request /users/admin/verify?login={}&password={}", login, password)
        return userService.findByLoginAndPassword(login, password)
    }
}