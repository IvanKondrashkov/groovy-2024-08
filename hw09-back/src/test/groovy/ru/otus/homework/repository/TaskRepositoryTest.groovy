package ru.otus.homework.repository

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.model.Task
import ru.otus.homework.mapper.TaskMapper
import io.micronaut.data.model.Sort
import io.micronaut.data.model.Pageable

@MicronautTest(startApplication = false)
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class TaskRepositoryTest extends Specification {
    @Inject
    UserRepository userRepository
    @Inject
    TaskRepository taskRepository
    @Inject
    TaskMapper taskMapper
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
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "update"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        task.name = "work"
        taskDb = taskRepository.update(task)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "findById"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        taskDb = taskRepository.findByInitiator_IdAndId(user.id, taskDb.id).orElseThrow()

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "findAll"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        def defaultPageable = Pageable.from(0, 10, Sort.of(Sort.Order.asc('id')))
        def tasks = taskRepository.findByInitiator_Id(user.id, defaultPageable).getContent().stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()

        then:
        tasks.size() == 1
    }

    def "deleteById"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        taskRepository.deleteById(taskDb.id)
        taskDb = taskRepository.findByInitiator_IdAndId(user.id, taskDb.id).orElse(null)

        then:
        taskDb == null
    }

    def "findAllByDate"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        def tasks = taskRepository.findAllByDate(user.id, taskDb.startDate).stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()

        then:
        tasks.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        def tasks = taskRepository.findAllByStartDateAndEndDate(user.id, taskDb.startDate, taskDb.endDate).stream()
                .map(it -> taskMapper::toTaskInfo(it))
                .collect()

        then:
        tasks.size() == 1
    }

    def "countAllByDate"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        def count = taskRepository.countAllByDate(user.id, taskDb.startDate)

        then:
        count == 1
    }
}