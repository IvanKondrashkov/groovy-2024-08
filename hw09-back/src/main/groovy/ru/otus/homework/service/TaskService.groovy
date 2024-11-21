package ru.otus.homework.service

import ru.otus.homework.dto.TaskInfo
import java.time.LocalDateTime

interface TaskService {
    List<TaskInfo> findAllByDate(LocalDateTime date)
    List<TaskInfo> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)
    long countAllByDate(LocalDateTime date)
}