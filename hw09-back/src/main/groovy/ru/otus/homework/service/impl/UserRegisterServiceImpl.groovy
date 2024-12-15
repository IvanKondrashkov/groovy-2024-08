package ru.otus.homework.service.impl

import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Inject
import jakarta.inject.Singleton
import ru.otus.homework.dto.UserInfo
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.model.User
import ru.otus.homework.repository.UserRepository
import ru.otus.homework.service.UserRegisterService

@Singleton
class UserRegisterServiceImpl implements UserRegisterService {
    @Inject
    UserRepository userRepository
    @Inject
    UserMapper userMapper

    @Transactional
    UserInfo save(User user) {
        def userDb = userRepository.save(user)
        return userMapper.toUserInfo(userDb)
    }
}