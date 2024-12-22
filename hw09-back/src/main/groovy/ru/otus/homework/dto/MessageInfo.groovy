package ru.otus.homework.dto

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@ToString
@Serdeable
@Introspected
class MessageInfo {
    UUID userId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime messageTs
}