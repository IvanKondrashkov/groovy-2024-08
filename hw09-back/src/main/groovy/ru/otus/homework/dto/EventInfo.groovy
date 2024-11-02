package ru.otus.homework.dto

import groovy.transform.ToString
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@ToString
@Serdeable
@Introspected
class EventInfo {
    UUID action_id
    String name
    LocalDateTime created

    EventInfo(UUID action_id, String name, LocalDateTime created) {
        this.action_id = action_id
        this.name = name
        this.created = created
    }
}