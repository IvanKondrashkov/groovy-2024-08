package ru.otus.homework.controller.client

import jakarta.validation.Valid
import jakarta.annotation.Nullable
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable
import static io.micronaut.http.HttpHeaders.AUTHORIZATION

@Client("/user")
interface ActionClient {
    @Post(uri="/{userId}/actions", produces="application/json")
    ActionInfo save(@Header(AUTHORIZATION) String authorization, @Body @Valid Action action, @PathVariable UUID userId)

    @Put(uri="/{userId}/actions/{id}", produces="application/json")
    ActionInfo updateById(@Header(AUTHORIZATION) String authorization, @Body @Valid Action action, @PathVariable UUID userId, @PathVariable UUID id)

    @Get(uri="/{userId}/actions/{id}", produces="application/json")
    ActionInfo findById(@Header(AUTHORIZATION) String authorization, @PathVariable UUID userId, @PathVariable UUID id)

    @Get(uri="/{userId}/actions",produces="application/json")
    List<ActionInfo> findAll(@Header(AUTHORIZATION) String authorization, @PathVariable UUID userId, Pageable pageable)

    @Delete(uri="/{userId}/actions/{id}", produces="application/json")
    void deleteById(@Header(AUTHORIZATION) String authorization, @PathVariable UUID userId, @PathVariable UUID id)

    @Get(uri="/actions/list{?userId,date}", produces="application/json")
    List<ActionInfo> findAllByDate(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date)

    @Get(uri="/actions/range{?userId,startDate,endDate}", produces="application/json")
    List<ActionInfo> findAllByStartDateAndEndDate(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate)

    @Get(uri="/actions/count{?userId,date}", produces="application/json")
    long countAllByDate(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable UUID userId, @QueryValue @Nullable LocalDateTime date)
}