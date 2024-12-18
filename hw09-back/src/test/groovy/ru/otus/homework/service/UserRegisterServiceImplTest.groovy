package ru.otus.homework.service

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.mapper.UserMapper

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class UserRegisterServiceImplTest extends Specification {
    @Inject
    UserMapper userMapper
    @Inject
    UserRegisterService userRegisterService
    User user

    def setup() {
        user = new User(
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
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
    }
}