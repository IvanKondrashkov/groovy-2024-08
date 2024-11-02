package ru.otus.homework.mapper

import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.context.annotation.Mapper

interface TaskMapper {
    @Mapper
    TaskInfo toTaskInfo(Task task)
}