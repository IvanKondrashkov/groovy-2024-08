package ru.otus.homework.model

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.EqualsAndHashCode
import jakarta.persistence.*
import java.time.LocalDateTime
import groovy.transform.ToString
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.Introspected

@Entity
@ToString
@Serdeable
@Introspected
@EqualsAndHashCode
@Table(name = "refresh_tokens")
class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id

    @NotNull
    @NotBlank
    String username

    @NotNull
    @NotBlank
    String refreshToken

    @NotNull
    Boolean revoked

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_created")
    LocalDateTime dateCreated

    RefreshToken(String username, String refreshToken, Boolean revoked, LocalDateTime dateCreated) {
        this.username = username
        this.refreshToken = refreshToken
        this.revoked = revoked
        this.dateCreated = dateCreated
    }

    RefreshToken() {

    }
}