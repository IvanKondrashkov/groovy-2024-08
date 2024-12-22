package ru.otus.homework.dto

import groovy.transform.ToString
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.Introspected

@ToString
@Serdeable
@Introspected
class UserInfo {
    UUID id
    String firstName
    String lastName
    String email

    UserInfo(UUID id, String firstName, String lastName, String email) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
    }
}