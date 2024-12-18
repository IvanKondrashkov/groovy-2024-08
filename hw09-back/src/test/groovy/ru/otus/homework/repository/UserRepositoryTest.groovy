package ru.otus.homework.repository

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import ru.otus.homework.model.User
import ru.otus.homework.mapper.UserMapper
import io.micronaut.data.model.Sort
import io.micronaut.data.model.Pageable

@MicronautTest(startApplication = false)
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class UserRepositoryTest extends Specification {
    @Inject
    UserRepository userRepository
    @Inject
    UserMapper userMapper
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
        def userDb = userRepository.save(user)

        then:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
        userDb.login == user.login
        userDb.password == user.password
        userDb.email == user.email
    }

    def "update"() {
        when:
        def userDb = userRepository.save(user)

        then:
        userDb.id != null

        when:
        userDb.firstName = "Nik"
        userDb = userRepository.update(user)

        then:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
        userDb.login == user.login
        userDb.password == user.password
        userDb.email == user.email
    }

    def "findById"() {
        when:
        def userDb = userRepository.save(user)

        then:
        userDb.id != null

        when:
        userDb = userRepository.findById(userDb.id).orElseThrow()

        then:
        userDb.id != null
        userDb.firstName == user.firstName
        userDb.lastName == user.lastName
        userDb.login == user.login
        userDb.password == user.password
        userDb.email == user.email
    }

    def "findAll"() {
        when:
        def userDb = userRepository.save(user)

        then:
        userDb.id != null

        when:
        def defaultPageable = Pageable.from(0, 10, Sort.of(Sort.Order.asc('id')))
        def users = userRepository.findAll(defaultPageable).getContent().stream()
                .map(it -> userMapper::toUserInfo(it))
                .collect()

        then:
        users.size() == 1
    }

    def "deleteById"() {
        when:
        def userDb = userRepository.save(user)

        then:
        userDb.id != null

        when:
        userRepository.deleteById(userDb.id)
        userDb = userRepository.findById(userDb.id).orElse(null)

        then:
        userDb == null
    }
}