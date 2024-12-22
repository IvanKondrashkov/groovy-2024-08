package ru.otus.homework.controller.client

import jakarta.validation.Valid
import jakarta.annotation.Nullable
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.*
import io.micronaut.data.model.Pageable
import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo
import static io.micronaut.http.HttpHeaders.AUTHORIZATION

@Client("/users/admin")
interface UserAdminClient {
    @Put(uri="/{id}", produces="application/json")
    UserInfo updateById(@Header(AUTHORIZATION) String authorization, @Body @Valid User user, @PathVariable UUID id)

    @Get(uri="/{id}", produces="application/json")
    UserInfo findById(@Header(AUTHORIZATION) String authorization, @PathVariable UUID id)

    @Get(produces="application/json")
    List<UserInfo> findAll(@Header(AUTHORIZATION) String authorization, Pageable pageable)

    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@Header(AUTHORIZATION) String authorization, @PathVariable UUID id)

    @Get(uri="/verify{?login,password}", produces="application/json")
    UserInfo findByLoginAndPassword(@Header(AUTHORIZATION) String authorization, @QueryValue @Nullable String login, @QueryValue @Nullable String password)
}