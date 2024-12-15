package ru.otus.homework.dto

import groovy.transform.ToString
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.Introspected
import ru.otus.homework.model.Action
import java.time.LocalDateTime

@ToString
@Serdeable
@Introspected
class TaskInfo {
    UUID id
    String name
    String description
    LocalDateTime startDate
    LocalDateTime endDate
    Set<Action> actions
    UserInfo initiator

    TaskInfo(UUID id, String name, String description, LocalDateTime startDate, LocalDateTime endDate, Set<Action> actions, UserInfo initiator) {
        this.id = id
        this.name = name
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
        this.actions = actions
        this.initiator = initiator
    }
}