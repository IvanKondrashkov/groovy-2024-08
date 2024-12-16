package ru.otus.homework.service

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import ru.otus.homework.repository.UserRepository
import spock.lang.Specification
import ru.otus.homework.model.User
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.exception.EntityNotFoundException

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class UserAdminServiceImplTest extends Specification {
    @Inject
    UserMapper userMapper
    @Inject
    UserRepository userRepository
    @Inject
    UserAdminService userAdminService
    User user

    def setup() {
        user = new User(
                firstName: "Djon",
                lastName: "Doe",
                login: "qwerty",
                password: "123456",
                email: "djon@mail.ru"
        )
        userRepository.save(user)
    }

    void cleanup() {
        user = null
    }

    def "findById"() {
        when:
        def userDb = userAdminService.findById(user.id)

        then:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
    }

    def "findByIdThrownEntityNotFoundException"() {
        when:
        userAdminService.findById(UUID.randomUUID())

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'User not found!'
    }

    def "findAll"() {
        when:
        def users = userAdminService.findAll(Pageable.from(0, 10))

        then:
        users.size() == 1
    }

    def "deleteById"() {
        when:
        userAdminService.deleteById(user.id)
        userAdminService.findById(user.id)

        then:
        def ex = thrown(EntityNotFoundException)
        ex.message == 'User not found!'
    }
}