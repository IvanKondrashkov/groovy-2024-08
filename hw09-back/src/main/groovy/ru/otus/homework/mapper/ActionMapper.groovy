package ru.otus.homework.mapper

import jakarta.inject.Inject
import jakarta.inject.Singleton
import ru.otus.homework.model.User
import ru.otus.homework.model.Action
import ru.otus.homework.dto.UserInfo
import ru.otus.homework.dto.ActionInfo
import io.micronaut.context.annotation.Mapper.Mapping

@Singleton
interface ActionMapper {
    @Inject
    UserMapper userMapper

    @Mapping(to = "initiator", from = "#{this.toUserInfo(action.initiator)}")
    ActionInfo toActionInfo(Action action)

    default UserInfo toUserInfo(User user) {
        return userMapper.toUserInfo(user)
    }
}