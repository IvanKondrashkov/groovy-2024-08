package ru.otus.homework.controller

import io.micronaut.http.HttpStatus
import jakarta.annotation.Nullable
import jakarta.inject.Inject
import jakarta.validation.Valid
import groovy.util.logging.Slf4j
import io.micronaut.validation.Validated
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.service.impl.ActionServiceImpl
import java.time.LocalDateTime

@Slf4j
@Validated
@Controller("/actions")
class ActionController {
    @Inject
    ActionServiceImpl actionService

    @Status(HttpStatus.CREATED)
    @Post(produces="application/json")
    ActionInfo save(@Body @Valid Action action) {
        log.info("Send save request /actions")
        return actionService.save(action)
    }

    @Put(uri="/{id}", produces="application/json")
    ActionInfo updateById(@Body @Valid Action action, @PathVariable UUID id) {
        log.info("Send update request /actions/{}", id)
        return actionService.updateById(action, id)
    }

    @Get(uri="/{id}", produces="application/json")
    ActionInfo findById(@PathVariable UUID id) {
        log.info("Send get request /actions/{}", id)
        return actionService.findById(id)
    }

    @Get(produces="application/json")
    List<ActionInfo> findAll(Pageable pageable) {
        log.info("Send get request /actions?page={}&size={}&sort={}", pageable.number, pageable.size, pageable.sort)
        return actionService.findAll(pageable)
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@PathVariable UUID id) {
        log.info("Send delete request /tasks/{}", id)
        actionService.deleteById(id)
    }

    @Get(uri="/list{?date}", produces="application/json")
    List<ActionInfo> findAllByDate(@QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /actions/list?date={}", date)
        return actionService.findAllByDate(date)
    }

    @Get(uri="/range{?startDate,endDate}", produces="application/json")
    List<ActionInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate) {
        log.info("Send get request /actions/range?startDate={}&endDate={}", startDate, endDate)
        return actionService.findAllByStartDateAndEndDate(startDate, endDate)
    }

    @Get(uri="/count{?date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /actions/count?date={}", date)
        return actionService.countAllByDate(date)
    }
}