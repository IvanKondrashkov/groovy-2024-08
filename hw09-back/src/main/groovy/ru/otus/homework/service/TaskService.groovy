package ru.otus.homework.service

import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.data.model.Pageable
import java.time.LocalDateTime

interface TaskService {
    TaskInfo save(Task task, UUID userId)
    TaskInfo updateById(Task task, UUID userId, UUID taskId)
    TaskInfo findById(UUID userId, UUID taskId)
    List<TaskInfo> findAll(UUID userId, Pageable pageable)
    void deleteById(UUID userId, UUID taskId)
    List<TaskInfo> findAllByDate(UUID userId, LocalDateTime date)
    List<TaskInfo> findAllByStartDateAndEndDate(UUID userId, LocalDateTime startDate, LocalDateTime endDate)
    long countAllByDate(UUID userId, LocalDateTime date)
}