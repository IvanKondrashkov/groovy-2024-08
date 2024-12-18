package ru.otus.homework.controller

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import ru.otus.homework.controller.client.UserRegisterClient
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.service.UserRegisterService
import ru.otus.homework.service.impl.UserRegisterServiceImpl

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class UserRegisterControllerTest extends Specification {
    @Inject
    UserRegisterClient userRegisterClient
    @Inject
    UserMapper userMapper
    @Inject
    UserRegisterService userRegisterService
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

    def "save"() {
        when:
        def userDb = userRegisterService.save(user)

        then:
        1 * userRegisterService.save(user) >> userMapper.toUserInfo(user)

        expect:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
    }

    @MockBean(UserRegisterServiceImpl)
    UserRegisterService userRegisterService() {
        Mock(UserRegisterService)
    }
}