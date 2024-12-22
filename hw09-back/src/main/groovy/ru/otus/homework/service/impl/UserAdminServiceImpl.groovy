package ru.otus.homework.service.impl

import io.micronaut.data.model.Sort
import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.transaction.annotation.Transactional
import ru.otus.homework.model.User
import ru.otus.homework.dto.UserInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.UserMapper
import ru.otus.homework.service.UserAdminService
import ru.otus.homework.repository.UserRepository
import ru.otus.homework.exception.EntityNotFoundException

@Singleton
class UserAdminServiceImpl implements UserAdminService {
    @Inject
    UserRepository userRepository
    @Inject
    UserMapper userMapper

    @Transactional
    UserInfo updateById(User user, UUID userId) {
        def userDb = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException('User not found!')
        )
        user.setId(userDb.id)
        userDb = userRepository.update(user)
        return userMapper.toUserInfo(userDb)
    }

    @Transactional(readOnly = true)
    UserInfo findById(UUID userId) {
        def userDb = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException('User not found!')
        )
        return userMapper.toUserInfo(userDb)
    }

    @Transactional(readOnly = true)
    List<UserInfo> findAll(Pageable pageable) {
        def defaultPageable = Pageable.from(pageable.number, pageable.size, Sort.of(Sort.Order.asc('id')))
        pageable = pageable.sort.isSorted() ? pageable : defaultPageable
        return userRepository.findAll(pageable).getContent().stream()
                .map(it -> userMapper::toUserInfo(it))
                .collect()
    }

    @Transactional
    void deleteById(UUID userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException('User not found!')
        )
        userRepository.deleteById(userId)
    }

    @Transactional(readOnly = true)
    UserInfo findByLoginAndPassword(String login, String password) {
        def userDb = userRepository.findByLoginAndPassword(login, password).orElseThrow(
                () -> new EntityNotFoundException('User not found!')
        )
        return userMapper.toUserInfo(userDb)
    }
}