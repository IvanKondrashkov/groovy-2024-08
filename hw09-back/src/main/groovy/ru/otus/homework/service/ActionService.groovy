package ru.otus.homework.service

import ru.otus.homework.dto.ActionInfo
import java.time.LocalDateTime

interface ActionService {
    List<ActionInfo> findAllByDate(LocalDateTime date)
    List<ActionInfo> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)
    long countAllByDate(LocalDateTime date)
}