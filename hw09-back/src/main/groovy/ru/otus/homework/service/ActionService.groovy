package ru.otus.homework.service

import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable
import java.time.LocalDateTime

interface ActionService {
    ActionInfo save(Action action, UUID userId)
    ActionInfo updateById(Action action, UUID userId, UUID actionId)
    ActionInfo findById(UUID userId, UUID actionId)
    List<ActionInfo> findAll(UUID userId, Pageable pageable)
    void deleteById(UUID userId, UUID actionId)
    List<ActionInfo> findAllByDate(UUID userId, LocalDateTime date)
    List<ActionInfo> findAllByStartDateAndEndDate(UUID userId, LocalDateTime startDate, LocalDateTime endDate)
    long countAllByDate(UUID userId, LocalDateTime date)
}