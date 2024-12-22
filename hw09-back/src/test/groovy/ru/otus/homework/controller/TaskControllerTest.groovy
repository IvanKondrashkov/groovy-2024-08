package ru.otus.homework.controller

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import ru.otus.homework.controller.client.AuthClient
import ru.otus.homework.controller.client.TaskClient

import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.model.Task
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.service.TaskService
import ru.otus.homework.service.impl.TaskServiceImpl
import ru.otus.homework.service.UserAdminService
import ru.otus.homework.service.UserRegisterService
import ru.otus.homework.service.impl.UserAdminServiceImpl
import ru.otus.homework.service.impl.UserRegisterServiceImpl
import io.micronaut.security.authentication.UsernamePasswordCredentials

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class TaskControllerTest extends Specification {
    @Inject
    AuthClient authClient
    @Inject
    TaskClient taskClient
    @Inject
    UserMapper userMapper
    @Inject
    TaskMapper taskMapper
    @Inject
    UserRegisterService userRegisterService
    @Inject
    UserAdminService userAdminService
    @Inject
    TaskService taskService
    User user
    Task task

    def setup() {
        user = new User(
                id: UUID.randomUUID(),
                firstName: "Djon",
                lastName: "Doe",
                login: "qwerty",
                password: "123456",
                email: "djon@mail.ru"
        )
        task = new Task(
                id: UUID.randomUUID(),
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0),
                actions: null,
                initiator: user
        )
    }

    void cleanup() {
        user = null
        task = null
    }

    def "save"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        def taskDb = taskClient.save(accessToken, task, user.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)

        expect:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "update"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        def taskDb = taskClient.findById(accessToken, user.id, task.id)
        taskDb != null
        task.name = "work"
        taskDb = taskClient.updateById(accessToken, task, user.id, task.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.findById(user.id, task.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.updateById(task, user.id, task.id) >> taskMapper.toTaskInfo(updateTask(task, "work"))

        expect:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "findById"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        def taskDb = taskClient.findById(accessToken, user.id, task.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.findById(user.id, task.id) >> taskMapper.toTaskInfo(task)

        expect:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
        taskDb.initiator.id == user.id
    }

    def "findAll"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        def tasks = taskClient.findAll(accessToken, user.id, Pageable.from(0, 10))

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.findAll(user.id, Pageable.from(0, 10)) >> List.of(taskMapper.toTaskInfo(task))

        expect:
        tasks.size() == 1
    }

    def "deleteById"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        taskClient.deleteById(accessToken, user.id, task.id)
        def taskDb = taskClient.findById(accessToken, user.id, task.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.deleteById(user.id, task.id)
        1 * taskService.findById(user.id, task.id) >> null

        then:
        taskDb == null
    }

    def "findAllByDate"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        def tasks = taskClient.findAllByDate(accessToken, user.id, task.startDate)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.findAllByDate(user.id, task.startDate) >> List.of(taskMapper.toTaskInfo(task))

        expect:
        tasks.size() == 1
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        def tasks = taskClient.findAllByStartDateAndEndDate(accessToken, user.id, task.startDate, task.endDate)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.findAllByStartDateAndEndDate(user.id, task.startDate, task.endDate) >> List.of(taskMapper.toTaskInfo(task))

        expect:
        tasks.size() == 1
    }

    def "countAllByDate"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        taskClient.save(accessToken, task, user.id)
        def count = taskClient.countAllByDate(accessToken, user.id, task.startDate)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * taskService.countAllByDate(user.id, task.startDate) >> 1

        expect:
        count == 1
    }

    @MockBean(UserRegisterServiceImpl)
    UserRegisterService userRegisterService() {
        Mock(UserRegisterService)
    }

    @MockBean(UserAdminServiceImpl)
    UserAdminService userAdminService() {
        Mock(UserAdminService)
    }

    @MockBean(TaskServiceImpl)
    TaskService taskService() {
        Mock(TaskService)
    }

    static Task updateTask(Task task, String name) {
        task.name = name
        return task
    }
}