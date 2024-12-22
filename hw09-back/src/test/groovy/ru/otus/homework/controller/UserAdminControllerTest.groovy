package ru.otus.homework.controller

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import ru.otus.homework.controller.client.AuthClient
import ru.otus.homework.controller.client.UserAdminClient
import spock.lang.Specification
import ru.otus.homework.model.User
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.service.UserAdminService
import ru.otus.homework.service.UserRegisterService
import ru.otus.homework.service.impl.UserAdminServiceImpl
import ru.otus.homework.service.impl.UserRegisterServiceImpl
import io.micronaut.security.authentication.UsernamePasswordCredentials

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class UserAdminControllerTest extends Specification {
    @Inject
    AuthClient authClient
    @Inject
    UserAdminClient userAdminClient
    @Inject
    UserMapper userMapper
    @Inject
    UserRegisterService userRegisterService
    @Inject
    UserAdminService userAdminService
    User user

    def setup() {
        user = new User(
                id: UUID.randomUUID(),
                firstName: "Djon",
                lastName: "Doe",
                login: "qwerty",
                password: "123456",
                email: "djon@mail.ru"
        )
    }

    void cleanup() {
        user = null
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
        def userDb = userAdminClient.findById(accessToken, user.id)
        userDb != null
        user.firstName = "Nik"
        userDb = userAdminClient.updateById(accessToken, user, user.id)

        then:
        1 * userAdminService.findById(user.id) >> userMapper.toUserInfo(user)
        1 * userAdminService.updateById(user, user.id) >> userMapper.toUserInfo(updateUser(user, "Nik"))

        expect:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
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
        def userDb = userAdminClient.findById(accessToken, user.id)

        then:
        1 * userAdminService.findById(user.id) >> userMapper.toUserInfo(user)

        expect:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
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
        def users = userAdminClient.findAll(accessToken, Pageable.from(0, 10))

        then:
        1 * userAdminService.findAll(Pageable.from(0, 10)) >> List.of(userMapper.toUserInfo(user))

        expect:
        users.size() == 1
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
        userAdminClient.deleteById(accessToken, user.id)
        def userDb = userAdminClient.findById(accessToken, user.id)

        then:
        1 * userAdminService.deleteById(user.id)
        1 * userAdminService.findById(user.id) >> null

        then:
        userDb == null
    }

    def "findByLoginAndPassword"() {
        when:
        userRegisterService.save(user)
        def auth = authClient.login(new UsernamePasswordCredentials(user.login, user.password))
        def accessToken = "${auth.tokenType} ${auth.accessToken}"

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        when:
        def userDb = userAdminClient.findByLoginAndPassword(accessToken, user.login, user.password)

        then:
        1 * userAdminService.findByLoginAndPassword(user.login, user.password) >> userMapper.toUserInfo(user)

        expect:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
    }

    @MockBean(UserRegisterServiceImpl)
    UserRegisterService userRegisterService() {
        Mock(UserRegisterService)
    }

    @MockBean(UserAdminServiceImpl)
    UserAdminService userAdminService() {
        Mock(UserAdminService)
    }

    static User updateUser(User user, String firstName) {
        user.firstName = firstName
        return user
    }
}