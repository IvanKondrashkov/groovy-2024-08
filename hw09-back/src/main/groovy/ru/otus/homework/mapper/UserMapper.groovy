package ru.otus.homework.mapper

import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo
import io.micronaut.context.annotation.Mapper

interface UserMapper {
    @Mapper
    UserInfo toUserInfo(User user)
}