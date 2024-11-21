package ru.otus.homework.mapper

import io.micronaut.context.annotation.Mapper
import ru.otus.homework.dto.ActionInfo
import ru.otus.homework.model.Action

interface ActionMapper {
    @Mapper
    ActionInfo toActionInfo(Action action)
}