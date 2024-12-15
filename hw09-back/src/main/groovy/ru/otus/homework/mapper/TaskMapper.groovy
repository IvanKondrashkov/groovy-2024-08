package ru.otus.homework.mapper

import jakarta.inject.Inject
import jakarta.inject.Singleton
import ru.otus.homework.model.User
import ru.otus.homework.model.Task
import ru.otus.homework.dto.UserInfo
import ru.otus.homework.dto.TaskInfo
import io.micronaut.context.annotation.Mapper.Mapping

@Singleton
interface TaskMapper {
    @Inject
    UserMapper userMapper

    @Mapping(to = "initiator", from = "#{this.toUserInfo(task.initiator)}")
    TaskInfo toTaskInfo(Task task)

    default UserInfo toUserInfo(User user) {
        return userMapper.toUserInfo(user)
    }
}