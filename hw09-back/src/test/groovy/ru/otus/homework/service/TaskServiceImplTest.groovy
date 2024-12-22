package ru.otus.homework.service

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.model.Task
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.repository.UserRepository
import ru.otus.homework.exception.EntityNotFoundException
import ru.otus.homework.exception.EntityNotValidException

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class TaskServiceImplTest extends Specification {
    @Inject
    TaskMapper taskMapper
    @Inject
    UserRepository userRepository
    @Inject
    TaskService taskService
    User user
    Task task

    def setup() {
        user = new User(
                firstName: "Djon",
                lastName: "Doe",
                login: "qwerty",
                password: "123456",
                email: "djon@mail.ru"
        )
        task = new Task(
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0),
                initiator: user
        )
        userRepository.save(user)
    }

    void cleanup() {
        user = null
        task = null
    }

    def "save"() {
        when:
        def taskDb = taskService.save(task, user.id)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "updateThrownEntityNotValidException"() {
        when:
        def taskDb = taskService.save(task, user.id)
        taskDb != null
        task.name = "work"
        taskService.updateById(task, user.id, taskDb.id)

        then:
        def ex = thrown(EntityNotValidException)
        ex.message == 'Task update, time not valid!'
    }

    def "findById"() {
        when:
        def taskDb = taskService.save(task, user.id)
        taskDb != null
        taskDb = taskService.findById(user.id, taskDb.id)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "findByIdThrownEntityNotFoundException"() {
        when:
        taskService.findById(user.id, UUID.randomUUID())

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Task not found!'
    }

    def "findAll"() {
        when:
        taskService.save(task, user.id)
        def tasks = taskService.findAll(user.id, Pageable.from(0, 10))

        then:
        tasks.size() == 1
    }

    def "deleteById"() {
        when:
        def taskDb = taskService.save(task, user.id)
        taskDb != null
        taskService.deleteById(user.id, taskDb.id)
        taskService.findById(user.id, taskDb.id)

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Task not found!'
    }

    def "findAllByDate"() {
        when:
        def taskDb = taskService.save(task, user.id)
        def tasks = taskService.findAllByDate(user.id, taskDb.startDate)

        then:
        tasks.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        def taskDb = taskService.save(task, user.id)
        def tasks = taskService.findAllByStartDateAndEndDate(user.id, taskDb.startDate, taskDb.endDate)

        then:
        tasks.size() == 1
    }

    def "countAllByDate"() {
        when:
        def taskDb = taskService.save(task, user.id)
        def count = taskService.countAllByDate(user.id, taskDb.startDate)

        then:
        count == 1
    }
}