package ru.otus.homework.controller

import jakarta.validation.Valid
import jakarta.annotation.Nullable
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.data.model.Pageable

@Client("/tasks")
interface TaskClient {
    @Post(produces="application/json")
    TaskInfo save(@Body @Valid Task task)

    @Put(uri="/{id}", produces="application/json")
    TaskInfo updateById(@Body @Valid Task task, @PathVariable UUID id)

    @Get(uri="/{id}", produces="application/json")
    TaskInfo findById(@PathVariable UUID id)

    @Get(produces="application/json")
    List<TaskInfo> findAll(Pageable pageable)

    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@PathVariable UUID id)

    @Get(uri="/list{?date}", produces="application/json")
    List<TaskInfo> findAllByDate(@QueryValue @Nullable LocalDateTime date)

    @Get(uri="/range{?startDate,endDate}", produces="application/json")
    List<TaskInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate)

    @Get(uri="/count{?date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable LocalDateTime date)
}