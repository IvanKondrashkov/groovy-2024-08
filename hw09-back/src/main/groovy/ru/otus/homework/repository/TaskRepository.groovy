package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.Task
import java.time.LocalDateTime

@Repository
interface TaskRepository extends PageableRepository<Task, UUID> {

    @Query(
            nativeQuery = true,
            value = "SELECT t.* FROM tasks t WHERE :date BETWEEN t.start_date AND t.end_date"
    )
    List<Task> findAllByDate(LocalDateTime date)

    @Query(
            nativeQuery = true,
            value = "SELECT t.* FROM tasks t WHERE :startDate BETWEEN t.start_date AND t.end_date OR :endDate BETWEEN t.start_date AND t.end_date"
    )
    List<Task> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(t.id) FROM tasks t WHERE :date BETWEEN t.start_date AND t.end_date"
    )
    long countAllByDate(LocalDateTime date)
}