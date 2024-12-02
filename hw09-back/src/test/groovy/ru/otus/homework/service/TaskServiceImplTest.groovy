package ru.otus.homework.service

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.Task
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.service.impl.TaskServiceImpl
import ru.otus.homework.exception.EntityNotFoundException
import ru.otus.homework.exception.EntityNotValidException

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class TaskServiceImplTest extends Specification {
    @Inject
    TaskMapper taskMapper
    @Inject
    TaskServiceImpl taskService
    Task task

    def setup() {
        task = new Task(
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0)
        )
    }

    void cleanup() {
        task = null
    }

    def "save"() {
        when:
        def taskDb = taskService.save(task)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "updateThrownEntityNotValidException"() {
        when:
        def taskDb = taskService.save(task)
        taskDb != null
        task.name = "work"
        taskService.updateById(task, taskDb.id)

        then:
        def ex = thrown(EntityNotValidException)
        ex.message == 'Task update, time not valid!'
    }

    def "findById"() {
        when:
        def taskDb = taskService.save(task)
        taskDb != null
        taskDb = taskService.findById(taskDb.id)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "findByIdThrownEntityNotFoundException"() {
        when:
        taskService.findById(UUID.randomUUID())

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Task not found!'
    }

    def "findAll"() {
        when:
        taskService.save(task)
        def tasks = taskService.findAll(Pageable.from(0, 10))

        then:
        tasks.size() == 1
    }

    def "deleteById"() {
        when:
        def taskDb = taskService.save(task)
        taskDb != null
        taskService.deleteById(taskDb.id)
        taskService.findById(taskDb.id)

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Task not found!'
    }

    def "findAllByDate"() {
        when:
        def taskDb = taskService.save(task)
        def tasks = taskService.findAllByDate(taskDb.startDate)

        then:
        tasks.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        def taskDb = taskService.save(task)
        def tasks = taskService.findAllByStartDateAndEndDate(taskDb.startDate, taskDb.endDate)

        then:
        tasks.size() == 1
    }

    def "countAllByDate"() {
        when:
        def taskDb = taskService.save(task)
        def count = taskService.countAllByDate(taskDb.startDate)

        then:
        count == 1
    }
}