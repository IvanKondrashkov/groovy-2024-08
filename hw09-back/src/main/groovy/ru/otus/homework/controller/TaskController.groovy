package ru.otus.homework.controller

import io.micronaut.http.HttpStatus
import jakarta.annotation.Nullable
import jakarta.inject.Inject
import jakarta.validation.Valid
import groovy.util.logging.Slf4j
import io.micronaut.validation.Validated
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.service.impl.TaskServiceImpl
import java.time.LocalDateTime

@Slf4j
@Validated
@Controller("/tasks")
class TaskController {
    @Inject
    TaskServiceImpl taskService

    @Status(HttpStatus.CREATED)
    @Post(produces="application/json")
    TaskInfo save(@Body @Valid Task task) {
        log.info("Send save request /tasks")
        return taskService.save(task)
    }

    @Put(uri="/{id}", produces="application/json")
    TaskInfo updateById(@Body @Valid Task task, @PathVariable UUID id) {
        log.info("Send update request /tasks/{}", id)
        return taskService.updateById(task, id)
    }

    @Get(uri="/{id}", produces="application/json")
    TaskInfo findById(@PathVariable UUID id) {
        log.info("Send get request /tasks/{}", id)
        return taskService.findById(id)
    }

    @Get(produces="application/json")
    List<TaskInfo> findAll(Pageable pageable) {
        log.info("Send get request /tasks?page={}&size={}&sort={}", pageable.number, pageable.size, pageable.sort)
        return taskService.findAll(pageable)
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@PathVariable UUID id) {
        log.info("Send delete request /tasks/{}", id)
        taskService.deleteById(id)
    }

    @Get(uri="/list{?date}", produces="application/json")
    List<TaskInfo> findAllByDate(@QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /tasks/list?date={}", date)
        return taskService.findAllByDate(date)
    }

    @Get(uri="/range{?startDate,endDate}", produces="application/json")
    List<TaskInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate) {
        log.info("Send get request /tasks/range?startDate={}&endDate={}", startDate, endDate)
        return taskService.findAllByStartDateAndEndDate(startDate, endDate)
    }

    @Get(uri="/count{?date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /tasks/count?date={}", date)
        return taskService.countAllByDate(date)
    }
}