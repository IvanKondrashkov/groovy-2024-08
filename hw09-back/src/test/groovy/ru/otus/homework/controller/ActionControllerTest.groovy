package ru.otus.homework.controller

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import ru.otus.homework.controller.client.ActionClient
import ru.otus.homework.controller.client.AuthClient
import ru.otus.homework.controller.client.TaskClient

import java.time.LocalDateTime
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.model.Task
import ru.otus.homework.model.Action
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.mapper.TaskMapper
import ru.otus.homework.mapper.ActionMapper
import ru.otus.homework.service.TaskService
import ru.otus.homework.service.ActionService
import ru.otus.homework.service.UserAdminService
import ru.otus.homework.service.UserRegisterService
import ru.otus.homework.service.impl.TaskServiceImpl
import ru.otus.homework.service.impl.ActionServiceImpl
import ru.otus.homework.service.impl.UserAdminServiceImpl
import ru.otus.homework.service.impl.UserRegisterServiceImpl
import io.micronaut.security.authentication.UsernamePasswordCredentials

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class ActionControllerTest extends Specification {
    @Inject
    AuthClient authClient
    @Inject
    TaskClient taskClient
    @Inject
    ActionClient actionClient
    @Inject
    UserMapper userMapper
    @Inject
    TaskMapper taskMapper
    @Inject
    ActionMapper actionMapper
    @Inject
    UserRegisterService userRegisterService
    @Inject
    UserAdminService userAdminService
    @Inject
    TaskService taskService
    @Inject
    ActionService actionService
    User user
    Task task
    Action action

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
        action = new Action(
                id: UUID.randomUUID(),
                name: "read",
                description: "read math",
                startDate: LocalDateTime.of(2024, 11, 2, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 3, 0, 0),
                task: task,
                initiator: user
        )
    }

    void cleanup() {
        user = null
        task = null
        action = null
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
        taskClient.save(accessToken, task, user.id)
        def actionDb = actionClient.save(accessToken, action, user.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)

        expect:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == task.id
        actionDb.initiator.id == user.id
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
        actionClient.save(accessToken, action, user.id)
        def actionDb = actionClient.findById(accessToken, user.id, action.id)
        actionDb != null
        action.name = "work"
        actionDb = actionClient.updateById(accessToken, action, user.id, action.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.findById(user.id, action.id) >> actionMapper.toActionInfo(action)
        1 * actionService.updateById(action, user.id, action.id) >> actionMapper.toActionInfo(updateAction(action, "work"))

        expect:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == task.id
        actionDb.initiator.id == user.id
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
        actionClient.save(accessToken, action, user.id)
        def actionDb = actionClient.findById(accessToken, user.id, action.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.findById(user.id, action.id) >> actionMapper.toActionInfo(action)

        expect:
        actionDb.id != null
        actionDb.name == action.name
        actionDb.description == action.description
        actionDb.startDate == action.startDate
        actionDb.endDate == action.endDate
        actionDb.task.id == task.id
        actionDb.initiator.id == user.id
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
        actionClient.save(accessToken, action, user.id)
        def actions = actionClient.findAll(accessToken, user.id, Pageable.from(0, 10))

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.findAll(user.id, Pageable.from(0, 10)) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
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
        actionClient.save(accessToken, action, user.id)
        actionClient.deleteById(accessToken, user.id, action.id)
        def actionDb = actionClient.findById(accessToken, user.id, action.id)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.deleteById(user.id, action.id)
        1 * actionService.findById(user.id, action.id) >> null

        then:
        actionDb == null
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
        actionClient.save(accessToken, action, user.id)
        def actions = actionClient.findAllByDate(accessToken, user.id, action.startDate)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.findAllByDate(user.id, action.startDate) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
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
        actionClient.save(accessToken, action, user.id)
        def actions = actionClient.findAllByStartDateAndEndDate(accessToken, user.id, action.startDate, action.endDate)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.findAllByStartDateAndEndDate(user.id, action.startDate, action.endDate) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
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
        actionClient.save(accessToken, action, user.id)
        def count = actionClient.countAllByDate(accessToken, user.id, action.startDate)

        then:
        1 * taskService.save(task, user.id) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action, user.id) >> actionMapper.toActionInfo(action)
        1 * actionService.countAllByDate(user.id, action.startDate) >> 1

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

    @MockBean(ActionServiceImpl)
    ActionService actionService() {
        Mock(ActionService)
    }

    static Action updateAction(Action action, String name) {
        action.name = name
        return action
    }
}