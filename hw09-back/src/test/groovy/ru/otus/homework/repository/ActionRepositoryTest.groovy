package ru.otus.homework.repository

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.Task
import ru.otus.homework.model.Action
import ru.otus.homework.mapper.ActionMapper
import io.micronaut.data.model.Sort
import io.micronaut.data.model.Pageable

@MicronautTest(startApplication = false)
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class ActionRepositoryTest extends Specification {
    @Inject
    TaskRepository taskRepository
    @Inject
    ActionRepository actionRepository
    @Inject
    ActionMapper actionMapper
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
        taskRepository.save(task)
    }

    void cleanup() {
        task = null
        action = null
    }

    def "save"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id

    }

    def "update"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        action.name = "work"
        actionDb = actionRepository.update(action)

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "findById"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        actionDb = actionRepository.findById(actionDb.id).orElseThrow()

        then:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "findAll"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        def defaultPageable = Pageable.from(0, 10, Sort.of(Sort.Order.asc('id')))
        def actions = actionRepository.findAll(defaultPageable).getContent().stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()

        then:
        actions.size() == 1
    }

    def "deleteById"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        actionRepository.deleteById(actionDb.id)
        actionDb = actionRepository.findById(actionDb.id).orElse(null)

        then:
        actionDb == null
    }

    def "findAllByDate"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        def actions = actionRepository.findAllByDate(actionDb.startDate).stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()

        then:
        actions.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        def actions = actionRepository.findAllByStartDateAndEndDate(actionDb.startDate, actionDb.endDate).stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()

        then:
        actions.size() == 1
    }

    def "countAllByDate"() {
        when:
        def actionDb = actionRepository.save(action)

        then:
        actionDb.id != null

        when:
        def count = actionRepository.countAllByDate(actionDb.startDate)

        then:
        count == 1
    }
}