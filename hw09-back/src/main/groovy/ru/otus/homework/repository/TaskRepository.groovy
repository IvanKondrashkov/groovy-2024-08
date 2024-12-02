package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.Task
import java.time.LocalDateTime

@Repository
interface TaskRepository extends PageableRepository<Task, UUID> {

    @Query("SELECT t FROM Task t WHERE :date BETWEEN t.startDate AND t.endDate")
    List<Task> findAllByDate(LocalDateTime date)

    @Query("SELECT t FROM Task t WHERE :startDate BETWEEN t.startDate AND t.endDate OR :endDate BETWEEN t.startDate AND t.endDate")
    List<Task> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)

    @Query("SELECT COUNT(t.id) FROM Task t WHERE :date BETWEEN t.startDate AND t.endDate")
    long countAllByDate(LocalDateTime date)
}