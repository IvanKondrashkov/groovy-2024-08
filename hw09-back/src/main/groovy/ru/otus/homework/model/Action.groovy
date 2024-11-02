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
@Table(name = "actions")
class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id

    @NotNull
    @NotBlank
    String name

    @NotNull
    @NotBlank
    String description

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_date")
    LocalDateTime startDate

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_date")
    LocalDateTime endDate

    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task

    Action(String name, String description, LocalDateTime startDate, LocalDateTime endDate, Task task) {
        this.name = name
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
        this.task = task
    }

    Action() {

    }
}