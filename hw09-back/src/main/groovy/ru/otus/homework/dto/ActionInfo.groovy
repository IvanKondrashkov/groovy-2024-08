package ru.otus.homework.dto

import groovy.transform.ToString
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.Introspected
import ru.otus.homework.model.Task
import java.time.LocalDateTime

@ToString
@Serdeable
@Introspected
class ActionInfo {
    UUID id
    String name
    String description
    LocalDateTime startDate
    LocalDateTime endDate
    Task task
    UserInfo initiator

    ActionInfo(UUID id, String name, String description, LocalDateTime startDate, LocalDateTime endDate, Task task, UserInfo initiator) {
        this.id = id
        this.name = name
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
        this.task = task
        this.initiator = initiator
    }
}