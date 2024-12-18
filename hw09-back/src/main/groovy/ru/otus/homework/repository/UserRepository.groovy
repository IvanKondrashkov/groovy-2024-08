package ru.otus.homework.repository

import io.micronaut.data.annotation.*;
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.User

@Repository
interface UserRepository extends PageableRepository<User, UUID> {
    Optional<User> findByLoginAndPassword(String login, String password)
}