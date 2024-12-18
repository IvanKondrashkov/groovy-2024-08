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
import ru.otus.homework.repository.UserRepository
import ru.otus.homework.exception.EntityNotValidException
import ru.otus.homework.exception.EntityNotFoundException
import java.time.LocalDateTime

@Singleton
class TaskServiceImpl implements TaskService {
    @Inject
    TaskRepository taskRepository
    @Inject
    UserRepository userRepository
    @Inject
    TaskMapper taskMapper

    @Transactional
    TaskInfo save(Task task, UUID userId) {
        def tasks = findAllByStartDateAndEndDate(userId, task.startDate, task.endDate)
        if (tasks.isEmpty()) {
            userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException('User not found!')
            )
            def taskDb = taskRepository.save(task)
            return taskMapper.toTaskInfo(taskDb)
        }
        throw new EntityNotValidException('Task create, time not valid!')
    }

    @Transactional
    TaskInfo updateById(Task task, UUID userId, UUID taskId) {
        def tasks = findAllByStartDateAndEndDate(userId, task.startDate, task.endDate)
        if (tasks.isEmpty()) {
            userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException('User not found!')
            )
            def taskDb = taskRepository.findById(taskId).orElseThrow(
                    () -> new EntityNotFoundException('Task not found!')
            )
            task.setId(taskDb.id)
            taskDb = taskRepository.update(task)
            return taskMapper.toTaskInfo(taskDb)
        }
        throw new EntityNotValidException('Task update, time not valid!')
    }

    @Transactional(readOnly = true)
    TaskInfo findById(UUID userId, UUID taskId) {
        def taskDb = taskRepository.findByInitiator_IdAndId(userId, taskId).orElseThrow(
                () -> new EntityNotFoundException('Task not found!')
        )
        return taskMapper.toTaskInfo(taskDb)
    }

    @Transactional(readOnly = true)
    List<TaskInfo> findAll(UUID userId, Pageable pageable) {
        def defaultPageable = Pageable.from(pageable.number, pageable.size, Sort.of(Sort.Order.asc('id')))
        pageable = pageable.sort.isSorted() ? pageable : defaultPageable
        return taskRepository.findByInitiator_Id(userId, pageable).getContent().stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()
    }

    @Transactional
    void deleteById(UUID userId, UUID taskId) {
        taskRepository.findByInitiator_IdAndId(userId, taskId).orElseThrow(
                () -> new EntityNotFoundException('Task not found!')
        )
        taskRepository.deleteById(taskId)
    }

    @Override
    @Transactional(readOnly = true)
    List<TaskInfo> findAllByDate(UUID userId, LocalDateTime date) {
        return taskRepository.findAllByDate(userId, date).stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()
    }

    @Override
    @Transactional(readOnly = true)
    List<TaskInfo> findAllByStartDateAndEndDate(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.findAllByStartDateAndEndDate(userId, startDate, endDate).stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()
    }

    @Override
    @Transactional(readOnly = true)
    long countAllByDate(UUID userId, LocalDateTime date) {
        return taskRepository.countAllByDate(userId, date)
    }
}