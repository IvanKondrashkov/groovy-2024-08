package ru.otus.homework.controller.client

import jakarta.validation.Valid
import jakarta.annotation.Nullable
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.data.model.Pageable
import static io.micronaut.http.HttpHeaders.AUTHORIZATION

@Client("/user")
interface TaskClient {
    @Post(uri="/{userId}/tasks", produces="application/json")
    TaskInfo save(@Header(AUTHORIZATION) String authorization, @Body @Valid Task task, @PathVariable UUID userId)

    @Put(uri="/{userId}/tasks/{id}", produces="application/json")
    TaskInfo updateById(@Header(AUTHORIZATION) String authorization, @Body @Valid Task task, @PathVariable UUID userId, @PathVariable UUID id)

    @Get(uri="/{userId}/tasks/{id}", produces="application/json")
    TaskInfo findById(@Header(AUTHORIZATION) String authorization, @PathVariable UUID userId, @PathVariable UUID id)

    @Get(uri="/{userId}/tasks",produces="application/json")
    List<TaskInfo> findAll(@Header(AUTHORIZATION) String authorization, @PathVariable UUID userId, Pageable pageable)

    @Delete(uri="/{userId}/tasks/{id}", produces="application/json")
    void deleteById(@Header(AUTHORIZATION) String authorization, @PathVariable UUID userId, @PathVariable UUID id)

    @Get(uri="/tasks/list{?userId,date}", produces="application/json")
    List<TaskInfo> findAllByDate(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date)

    @Get(uri="/tasks/range{?userId,startDate,endDate}", produces="application/json")
    List<TaskInfo> findAllByStartDateAndEndDate(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate)

    @Get(uri="/tasks/count{?userId,date}", produces="application/json")
    long countAllByDate(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date)
}