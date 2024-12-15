package ru.otus.homework.service

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.Task
import ru.otus.homework.model.Action
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.mapper.ActionMapper
import ru.otus.homework.service.impl.TaskServiceImpl
import ru.otus.homework.service.impl.ActionServiceImpl
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
    TaskServiceImpl taskService
    @Inject
    ActionServiceImpl actionService
    Task task
    Action action

    def setup() {
        task = new Task(
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0)
        )
        action = new Action(
                name: "read",
                description: "read math",
                startDate: LocalDateTime.of(2024, 11, 2, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 3, 0, 0),
                task: task
        )
    }

    void cleanup() {
        task = null
    }

    def "save"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "updateThrownEntityNotValidException"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)
        actionDb != null
        action.name = "work"
        actionService.updateById(action, actionDb.id)

        then:
        def ex = thrown(EntityNotValidException)
        ex.message == 'Action update, time not valid!'
    }

    def "findById"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)
        actionDb != null
        actionDb = actionService.findById(actionDb.id)

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "findByIdThrownEntityNotFoundException"() {
        when:
        actionService.findById(UUID.randomUUID())

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Action not found!'
    }

    def "findAll"() {
        when:
        taskService.save(task)
        actionService.save(action)
        def actions = actionService.findAll(Pageable.from(0, 10))

        then:
        actions.size() == 1
    }

    def "deleteById"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)
        actionDb != null
        actionService.deleteById(actionDb.id)
        actionService.findById(actionDb.id)

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'Action not found!'
    }

    def "findAllByDate"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)
        def actions = actionService.findAllByDate(actionDb.startDate)

        then:
        actions.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)
        def actions = actionService.findAllByStartDateAndEndDate(actionDb.startDate, actionDb.endDate)

        then:
        actions.size() == 1
    }

    def "countAllByDate"() {
        when:
        taskService.save(task)
        def actionDb = actionService.save(action)
        def count = actionService.countAllByDate(actionDb.startDate)

        then:
        count == 1
    }
}