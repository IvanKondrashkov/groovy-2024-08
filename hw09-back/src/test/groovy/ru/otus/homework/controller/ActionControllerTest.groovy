package ru.otus.homework.controller

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
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

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class ActionControllerTest extends Specification {
    @Inject
    TaskClient taskClient
    @Inject
    ActionClient actionClient
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
                id: UUID.randomUUID(),
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0),
                actions: null
        )
        action = new Action(
                id: UUID.randomUUID(),
                name: "read",
                description: "read math",
                startDate: LocalDateTime.of(2024, 11, 2, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 3, 0, 0),
                task: task
        )
    }

    void cleanup() {
        task = null
        action = null
    }

    def "save"() {
        when:
        taskClient.save(task)
        def actionDb = actionClient.save(action)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)

        expect:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "update"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def actionDb = actionClient.findById(action.id)
        actionDb != null
        action.name = "work"
        actionDb = actionClient.updateById(action, action.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.findById(action.id) >> actionMapper.toActionInfo(action)
        1 * actionService.updateById(action, action.id) >> actionMapper.toActionInfo(updateAction(action, "work"))

        expect:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "findById"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def actionDb = actionClient.findById(action.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.findById(action.id) >> actionMapper.toActionInfo(action)

        expect:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == action.task.id
    }

    def "findAll"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def actions = actionClient.findAll(Pageable.from(0, 10))

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.findAll(Pageable.from(0, 10)) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
    }

    def "deleteById"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        actionClient.deleteById(action.id)
        def actionDb = actionClient.findById(action.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.deleteById(action.id)
        1 * actionService.findById(action.id) >> null

        then:
        actionDb == null
    }

    def "findAllByDate"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def actions = actionClient.findAllByDate(action.startDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.findAllByDate(action.startDate) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def actions = actionClient.findAllByStartDateAndEndDate(action.startDate, action.endDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.findAllByStartDateAndEndDate(action.startDate, action.endDate) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
    }

    def "countAllByDate"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def count = actionClient.countAllByDate(action.startDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.countAllByDate(action.startDate) >> 1

        expect:
        count == 1
    }

    @MockBean(TaskServiceImpl)
    TaskServiceImpl taskService() {
        Mock(TaskServiceImpl)
    }

    @MockBean(ActionServiceImpl)
    ActionServiceImpl actionService() {
        Mock(ActionServiceImpl)
    }

    static Action updateAction(Action action, String name) {
        action.name = name
        return action
    }
}