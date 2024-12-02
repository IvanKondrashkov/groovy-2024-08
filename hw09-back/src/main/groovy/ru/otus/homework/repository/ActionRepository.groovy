package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.Action
import java.time.LocalDateTime

@Repository
interface ActionRepository extends PageableRepository<Action, UUID> {

    @Query("SELECT a FROM Action a WHERE :date BETWEEN a.startDate AND a.endDate")
    List<Action> findAllByDate(LocalDateTime date)

    @Query("SELECT a FROM Action a WHERE :startDate BETWEEN a.startDate AND a.endDate OR :endDate BETWEEN a.startDate AND a.endDate")
    List<Action> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)

    @Query("SELECT COUNT(a.id) FROM Action a WHERE :date BETWEEN a.startDate AND a.endDate")
    long countAllByDate(LocalDateTime date)
}