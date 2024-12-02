package ru.otus.homework.controller

import jakarta.validation.Valid
import jakarta.annotation.Nullable
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime
import io.micronaut.http.annotation.*
import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable

@Client("/actions")
interface ActionClient {
    @Post(produces="application/json")
    ActionInfo save(@Body @Valid Action action)

    @Put(uri="/{id}", produces="application/json")
    ActionInfo updateById(@Body @Valid Action action, @PathVariable UUID id)

    @Get(uri="/{id}", produces="application/json")
    ActionInfo findById(@PathVariable UUID id)

    @Get(produces="application/json")
    List<ActionInfo> findAll(Pageable pageable)

    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@PathVariable UUID id)

    @Get(uri="/list{?date}", produces="application/json")
    List<ActionInfo> findAllByDate(@QueryValue @Nullable LocalDateTime date)

    @Get(uri="/range{?startDate,endDate}", produces="application/json")
    List<ActionInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate)

    @Get(uri="/count{?date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable LocalDateTime date)
}