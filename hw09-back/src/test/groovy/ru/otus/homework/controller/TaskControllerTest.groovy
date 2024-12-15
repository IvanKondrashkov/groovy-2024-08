package ru.otus.homework.controller

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.Task
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.service.impl.TaskServiceImpl

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class TaskControllerTest extends Specification {
    @Inject
    TaskClient taskClient
    @Inject
    TaskMapper taskMapper
    @Inject
    TaskServiceImpl taskService
    Task task

    def setup() {
        task = new Task(
                id: UUID.randomUUID(),
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0),
                actions: null
        )
    }

    void cleanup() {
        task = null
    }

    def "save"() {
        when:
        def taskDb = taskClient.save(task)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)

        expect:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "update"() {
        when:
        taskClient.save(task)
        def taskDb = taskClient.findById(task.id)
        taskDb != null
        task.name = "work"
        taskDb = taskClient.updateById(task, task.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.findById(task.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.updateById(task, task.id) >> taskMapper.toTaskInfo(updateTask(task, "work"))

        expect:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "findById"() {
        when:
        taskClient.save(task)
        def taskDb = taskClient.findById(task.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.findById(task.id) >> taskMapper.toTaskInfo(task)

        expect:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "findAll"() {
        when:
        taskClient.save(task)
        def tasks = taskClient.findAll(Pageable.from(0, 10))

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.findAll(Pageable.from(0, 10)) >> List.of(taskMapper.toTaskInfo(task))

        expect:
        tasks.size() == 1
    }

    def "deleteById"() {
        when:
        taskClient.save(task)
        taskClient.deleteById(task.id)
        def taskDb = taskClient.findById(task.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.deleteById(task.id)
        1 * taskService.findById(task.id) >> null

        then:
        taskDb == null
    }

    def "findAllByDate"() {
        when:
        taskClient.save(task)
        def tasks = taskClient.findAllByDate(task.startDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.findAllByDate(task.startDate) >> List.of(taskMapper.toTaskInfo(task))

        expect:
        tasks.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        taskClient.save(task)
        def tasks = taskClient.findAllByStartDateAndEndDate(task.startDate, task.endDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.findAllByStartDateAndEndDate(task.startDate, task.endDate) >> List.of(taskMapper.toTaskInfo(task))

        expect:
        tasks.size() == 1
    }

    def "countAllByDate"() {
        when:
        taskClient.save(task)
        def count = taskClient.countAllByDate(task.startDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * taskService.countAllByDate(task.startDate) >> 1

        expect:
        count == 1
    }

    @MockBean(TaskServiceImpl)
    TaskServiceImpl taskService() {
        Mock(TaskServiceImpl)
    }

    static Task updateTask(Task task, String name) {
        task.name = name
        return task
    }
}