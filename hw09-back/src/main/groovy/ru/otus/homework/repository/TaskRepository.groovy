package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.Task
import java.time.LocalDateTime

@Repository
interface TaskRepository extends PageableRepository<Task, UUID> {

    Optional<Task> findByInitiator_IdAndId(UUID userId, UUID taskId)

    Page<Task> findByInitiator_Id(UUID userId, Pageable pageable)

    @Query("SELECT t FROM Task t WHERE :userId = t.initiator.id AND :date BETWEEN t.startDate AND t.endDate")
    List<Task> findAllByDate(UUID userId, LocalDateTime date)

    @Query("SELECT t FROM Task t WHERE :userId = t.initiator.id AND :startDate BETWEEN t.startDate AND t.endDate OR :endDate BETWEEN t.startDate AND t.endDate")
    List<Task> findAllByStartDateAndEndDate(UUID userId, LocalDateTime startDate, LocalDateTime endDate)

    @Query("SELECT COUNT(t.id) FROM Task t WHERE :userId = t.initiator.id AND :date BETWEEN t.startDate AND t.endDate")
    long countAllByDate(UUID userId, LocalDateTime date)
}