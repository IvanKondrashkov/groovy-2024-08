package ru.otus.homework.repository

import io.micronaut.data.annotation.*
import io.micronaut.data.repository.PageableRepository
import ru.otus.homework.model.RefreshToken

@Repository
interface RefreshTokenRepository extends PageableRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken)
}