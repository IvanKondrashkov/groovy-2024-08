package ru.otus.homework.service.impl

import io.micronaut.data.model.Sort
import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.transaction.annotation.Transactional
import ru.otus.homework.model.Task
import ru.otus.homework.dto.TaskInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.service.TaskService
import ru.otus.homework.repository.TaskRepository
import ru.otus.homework.exception.EntityNotValidException
import ru.otus.homework.exception.EntityNotFoundException
import java.time.LocalDateTime

@Singleton
class TaskServiceImpl implements TaskService {
    @Inject
    TaskRepository taskRepository
    @Inject
    TaskMapper taskMapper

    @Transactional
    TaskInfo save(Task task) {
        def tasks = findAllByStartDateAndEndDate(task.startDate, task.endDate)
        if (tasks.isEmpty()) {
            def taskDb = taskRepository.save(task)
            def taskInfo = taskMapper.toTaskInfo(taskDb)
            return taskInfo
        }
        throw new EntityNotValidException('Task create, time not valid!')
    }

    @Transactional
    TaskInfo updateById(Task task, UUID taskId) {
        def tasks = findAllByStartDateAndEndDate(task.startDate, task.endDate)
        if (tasks.isEmpty()) {
            def taskDb = taskRepository.findById(taskId).orElseThrow(
                    () -> new EntityNotFoundException('Task not found!')
            )
            task.setId(taskDb.id)
            taskDb = taskRepository.update(task)
            def taskInfo = taskMapper.toTaskInfo(taskDb)
            return taskInfo
        }
        throw new EntityNotValidException('Task update, time not valid!')
    }

    @Transactional(readOnly = true)
    TaskInfo findById(UUID taskId) {
        def taskDb = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException('Task not found!')
        )
        return taskMapper.toTaskInfo(taskDb)
    }

    @Transactional(readOnly = true)
    List<TaskInfo> findAll(Pageable pageable) {
        def defaultPageable = Pageable.from(pageable.number, pageable.size, Sort.of(Sort.Order.asc('id')))
        pageable = pageable.sort.isSorted() ? pageable : defaultPageable
        return taskRepository.findAll(pageable).getContent().stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()
    }

    @Transactional
    void deleteById(UUID taskId) {
        taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException('Task not found!')
        )
        taskRepository.deleteById(taskId)
    }

    @Override
    @Transactional(readOnly = true)
    List<TaskInfo> findAllByDate(LocalDateTime date) {
        return taskRepository.findAllByDate(date).stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()
    }

    @Override
    @Transactional(readOnly = true)
    List<TaskInfo> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.findAllByStartDateAndEndDate(startDate, endDate).stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()
    }

    @Override
    @Transactional(readOnly = true)
    long countAllByDate(LocalDateTime date) {
        return taskRepository.countAllByDate(date)
    }
}