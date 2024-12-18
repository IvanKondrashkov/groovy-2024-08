package ru.otus.homework.service.impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.core.async.publisher.Publishers
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import org.reactivestreams.Publisher
import ru.otus.homework.model.RefreshToken
import ru.otus.homework.service.UserAdminService
import ru.otus.homework.repository.RefreshTokenRepository
import java.time.LocalDateTime
import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT

@Singleton
class AuthProviderImpl<B> implements HttpRequestAuthenticationProvider<B>, RefreshTokenPersistence {
    @Inject
    UserAdminService userService
    @Inject
    RefreshTokenRepository refreshTokenRepository

    @Override
    AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {
        def userDb = userService.findByLoginAndPassword(authRequest.getIdentity(), authRequest.getSecret())
        return userDb != null
                ? AuthenticationResponse.success(authRequest.getIdentity())
                : AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
    }

    @Override
    void persistToken(RefreshTokenGeneratedEvent event) {
        if (event?.refreshToken && event?.authentication?.name) {
            def refreshToken = new  RefreshToken(
                    username: event.authentication.name,
                    refreshToken: event.refreshToken,
                    revoked: false,
                    dateCreated: LocalDateTime.now()
            )
            refreshTokenRepository.save(refreshToken)
        }
    }

    @Override
    Publisher<Authentication> getAuthentication(String refreshToken) {
        def refreshTokenDb = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new OauthErrorResponseException(INVALID_GRANT, 'Refresh token not found!', null)
        )

        if (refreshTokenDb.getRevoked()) {
            throw new OauthErrorResponseException(INVALID_GRANT, 'Refresh token revoked!', null)
        }

        return Optional.ofNullable(refreshTokenDb)
                .map(principal -> Authentication.build(principal.getUsername()))
                .map(Publishers::just)
                .orElseGet(Publishers::empty) as Publisher<Authentication>
    }
}