package ru.otus.homework.controller.client

import jakarta.validation.Valid
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.security.authentication.UsernamePasswordCredentials

@Client("/")
interface AuthClient {
    @Post(uri="login", produces="application/json")
    BearerAccessRefreshToken login(@Body @Valid UsernamePasswordCredentials credentials)
}