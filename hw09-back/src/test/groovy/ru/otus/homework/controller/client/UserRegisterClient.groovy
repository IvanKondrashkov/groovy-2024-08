package ru.otus.homework.controller.client

import jakarta.validation.Valid
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.*
import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo

@Client("/users/register")
interface UserRegisterClient {
    @Post(produces="application/json")
    UserInfo save(@Body @Valid User user)
}