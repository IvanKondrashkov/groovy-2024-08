package ru.otus.homework.model

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.EqualsAndHashCode
import jakarta.persistence.*
import groovy.transform.ToString
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.Introspected

@Entity
@ToString
@Serdeable
@Introspected
@EqualsAndHashCode
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id

    @NotNull
    @NotBlank
    String firstName

    @NotNull
    @NotBlank
    String lastName

    @NotNull
    @NotBlank
    String login

    @NotNull
    @NotBlank
    String password

    @NotNull
    @NotBlank
    @Email
    String email

    User(String firstName, String lastName, String login, String password, String email) {
        this.firstName = firstName
        this.lastName = lastName
        this.login = login
        this.password = password
        this.email = email
    }

    User() {

    }
}