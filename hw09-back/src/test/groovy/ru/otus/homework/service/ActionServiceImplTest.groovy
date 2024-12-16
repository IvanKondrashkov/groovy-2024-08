package ru.otus.homework.service

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.model.Task
import ru.otus.homework.model.Action
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.mapper.ActionMapper
import ru.otus.homework.repository.UserRepository
import ru.otus.homework.repository.TaskRepository
import ru.otus.homework.exception.EntityNotFoundException
import ru.otus.homework.exception.EntityNotValidException

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class ActionServiceImplTest extends Specification {
    @Inject
    TaskMapper taskMapper
    @Inject
    ActionMapper actionMapper
    @Inject
    UserRepository userRepository
    @Inject
    TaskRepository taskRepository
    @Inject
    ActionService actionService
    User user
    Task task
    Action action

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
        action = new Action(
                name: "read",
                description: "read math",
                startDate: LocalDateTime.of(2024, 11, 2, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 3, 0, 0),
                task: task,
                initiator: user
        )
        userRepository.save(user)
        taskRepository.save(task)
    }

    void cleanup() {
        user = null
        task = null
        action = null
    }

    def "save"() {
        when:
        def actionDb = actionService.save(action, user.id)

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == task.id
        actionDb.initiator.id == user.id
    }

    def "updateThrownEntityNotValidException"() {
        when:
        def actionDb = actionService.save(action, user.id)
        actionDb != null
        action.name = "work"
        actionService.updateById(action, user.id, actionDb.id)

        then:
        def ex = thrown(EntityNotValidException)
        ex.message == 'Action update, time not valid!'
    }

    def "findById"() {
        when:
        def actionDb = actionService.save(action, user.id)
        actionDb != null
        actionDb = actionService.findById(user.id, actionDb.id)

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == task.id
        actionDb.initiator.id == user.id
    }

    def "findByIdThrownEntityNotFoundException"() {
        when:
        actionService.findById(user.id, UUID.randomUUID())

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Action not found!'
    }

    def "findAll"() {
        when:
        actionService.save(action, user.id)
        def actions = actionService.findAll(user.id, Pageable.from(0, 10))

        then:
        actions.size() == 1
    }

    def "deleteById"() {
        when:
        def actionDb = actionService.save(action, user.id)
        actionDb != null
        actionService.deleteById(user.id, actionDb.id)
        actionService.findById(user.id, actionDb.id)

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Action not found!'
    }

    def "findAllByDate"() {
        when:
        def actionDb = actionService.save(action, user.id)
        def actions = actionService.findAllByDate(user.id, actionDb.startDate)

        then:
        actions.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        def actionDb = actionService.save(action, user.id)
        def actions = actionService.findAllByStartDateAndEndDate(user.id, actionDb.startDate, actionDb.endDate)

        then:
        actions.size() == 1
    }

    def "countAllByDate"() {
        when:
        def actionDb = actionService.save(action, user.id)
        def count = actionService.countAllByDate(user.id, actionDb.startDate)

        then:
        count == 1
    }
}