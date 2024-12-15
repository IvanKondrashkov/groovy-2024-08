package ru.otus.homework.controller

import io.micronaut.http.HttpStatus
import jakarta.annotation.Nullable
import jakarta.inject.Inject
import jakarta.validation.Valid
import groovy.util.logging.Slf4j
import io.micronaut.validation.Validated
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.service.ActionService
import java.time.LocalDateTime

@Slf4j
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user")
class ActionController {
    @Inject
    ActionService actionService

    @Status(HttpStatus.CREATED)
    @Post(uri="/{userId}/actions", produces="application/json")
    ActionInfo save(@Body @Valid Action action, @PathVariable UUID userId) {
        log.info("Send save request /user/{}/actions", userId)
        return actionService.save(action, userId)
    }

    @Put(uri="/{userId}/actions/{id}", produces="application/json")
    ActionInfo updateById(@Body @Valid Action action, @PathVariable UUID userId, @PathVariable UUID id) {
        log.info("Send update request /user/{}/actions/{}", userId, id)
        return actionService.updateById(action, userId, id)
    }

    @Get(uri="/{userId}/actions/{id}", produces="application/json")
    ActionInfo findById(@PathVariable UUID userId, @PathVariable UUID id) {
        log.info("Send get request /user/{}/actions/{}", userId, id)
        return actionService.findById(userId, id)
    }

    @Get(uri="/{userId}/actions",produces="application/json")
    List<ActionInfo> findAll(@PathVariable UUID userId, Pageable pageable) {
        log.info("Send get request /user/{}/actions?page={}&size={}&sort={}", userId, pageable.number, pageable.size, pageable.sort)
        return actionService.findAll(userId, pageable)
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri="/{userId}/actions/{id}", produces="application/json")
    void deleteById(@PathVariable UUID userId, @PathVariable UUID id) {
        log.info("Send delete request /user/{}/actions/{}", userId, id)
        actionService.deleteById(userId, id)
    }

    @Get(uri="/actions/list{?userId,date}", produces="application/json")
    List<ActionInfo> findAllByDate(@QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /user/actions/list?userId={}&date={}", userId, date)
        return actionService.findAllByDate(userId, date)
    }

    @Get(uri="/actions/range{?userId,startDate,endDate}", produces="application/json")
    List<ActionInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate) {
        log.info("Send get request /user/actions/range?userId={}&startDate={}&endDate={}", userId, startDate, endDate)
        return actionService.findAllByStartDateAndEndDate(userId, startDate, endDate)
    }

    @Get(uri="/actions/count{?userId,date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /user/actions/count?userId={}&date={}", userId, date)
        return actionService.countAllByDate(userId, date)
    }
}