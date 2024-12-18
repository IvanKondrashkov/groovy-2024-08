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
import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.service.TaskService
import java.time.LocalDateTime

@Slf4j
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user")
class TaskController {
    @Inject
    TaskService taskService

    @Status(HttpStatus.CREATED)
    @Post(uri="/{userId}/tasks", produces="application/json")
    TaskInfo save(@Body @Valid Task task, @PathVariable UUID userId) {
        log.info("Send save request /user/{}/tasks", userId)
        return taskService.save(task, userId)
    }

    @Put(uri="/{userId}/tasks/{id}", produces="application/json")
    TaskInfo updateById(@Body @Valid Task task, @PathVariable UUID userId, @PathVariable UUID id) {
        log.info("Send update request /user/{}/tasks/{}", userId, id)
        return taskService.updateById(task, userId, id)
    }

    @Get(uri="/{userId}/tasks/{id}", produces="application/json")
    TaskInfo findById(@PathVariable UUID userId, @PathVariable UUID id) {
        log.info("Send get request /user/{}/tasks/{}", userId, id)
        return taskService.findById(userId, id)
    }

    @Get(uri="/{userId}/tasks",produces="application/json")
    List<TaskInfo> findAll(@PathVariable UUID userId, Pageable pageable) {
        log.info("Send get request /user/{}/tasks?page={}&size={}&sort={}", userId, pageable.number, pageable.size, pageable.sort)
        return taskService.findAll(userId, pageable)
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri="/{userId}/tasks/{id}", produces="application/json")
    void deleteById(@PathVariable UUID userId, @PathVariable UUID id) {
        log.info("Send delete request /user/{}/tasks/{}", userId, id)
        taskService.deleteById(userId, id)
    }

    @Get(uri="/tasks/list{?userId,date}", produces="application/json")
    List<TaskInfo> findAllByDate(@QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /user/tasks/list?userId={}&date={}", userId, date)
        return taskService.findAllByDate(userId, date)
    }

    @Get(uri="/tasks/range{?userId,startDate,endDate}", produces="application/json")
    List<TaskInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate) {
        log.info("Send get request /user/tasks/range?userId={}&startDate={}&endDate={}", userId, startDate, endDate)
        return taskService.findAllByStartDateAndEndDate(userId, startDate, endDate)
    }

    @Get(uri="/tasks/count{?userId,date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /user/tasks/count?userId={}&date={}", userId, date)
        return taskService.countAllByDate(userId, date)
    }
}