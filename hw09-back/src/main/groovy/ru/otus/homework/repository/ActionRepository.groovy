package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.Action
import java.time.LocalDateTime

@Repository
interface ActionRepository extends PageableRepository<Action, UUID> {

    Optional<Action> findByInitiator_IdAndId(UUID userId, UUID actionId)

    Page<Action> findByInitiator_Id(UUID userId, Pageable pageable)

    @Query("SELECT a FROM Action a WHERE :userId = a.initiator.id AND :date BETWEEN a.startDate AND a.endDate")
    List<Action> findAllByDate(UUID userId, LocalDateTime date)

    @Query("SELECT a FROM Action a WHERE :userId = a.initiator.id AND :startDate BETWEEN a.startDate AND a.endDate OR :endDate BETWEEN a.startDate AND a.endDate")
    List<Action> findAllByStartDateAndEndDate(UUID userId, LocalDateTime startDate, LocalDateTime endDate)

    @Query("SELECT COUNT(a.id) FROM Action a WHERE :userId = a.initiator.id AND :date BETWEEN a.startDate AND a.endDate")
    long countAllByDate(UUID userId, LocalDateTime date)
}