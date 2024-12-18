package ru.otus.homework.service

import ru.otus.homework.dto.UserInfo
import ru.otus.homework.model.User

interface UserRegisterService {
    UserInfo save(User user)
}