package ru.otus.homework.service

import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo
import io.micronaut.data.model.Pageable

interface UserAdminService {
    UserInfo updateById(User user, UUID userId)
    UserInfo findById(UUID userId)
    List<UserInfo> findAll(Pageable pageable)
    void deleteById(UUID userId)
    UserInfo findByLoginAndPassword(String login, String password)
}