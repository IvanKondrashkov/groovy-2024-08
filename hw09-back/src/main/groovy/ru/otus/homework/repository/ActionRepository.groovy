package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.Action
import java.time.LocalDateTime

@Repository
interface ActionRepository extends PageableRepository<Action, UUID> {

    @Query(
            nativeQuery = true,
            value = "SELECT a.* FROM actions a WHERE :date BETWEEN a.start_date AND a.end_date"
    )
    List<Action> findAllByDate(LocalDateTime date)

    @Query(
            nativeQuery = true,
            value = "SELECT a.* FROM actions a WHERE :startDate BETWEEN a.start_date AND a.end_date OR :endDate BETWEEN a.start_date AND a.end_date"
    )
    List<Action> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(a.id) FROM actions a WHERE :date BETWEEN a.start_date AND a.end_date"
    )
    long countAllByDate(LocalDateTime date)
}