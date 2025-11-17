package com.wbprofit.feature.auth.impl.data

import com.wbprofit.core.keystore.api.KeystoreFeature
import com.wbprofit.core.keystore.api.SecureStorageKeys
import com.wbprofit.feature.auth.api.AuthVerificationResult
import com.wbprofit.feature.auth.impl.data.network.AuthApi
import com.wbprofit.feature.auth.impl.domain.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.concurrent.TimeUnit

private const val DEFAULT_RETRY_AFTER_MILLIS = 30_000L

internal class AuthRepositoryImpl(
    private val api: AuthApi,
    private val secureStorage: KeystoreFeature,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {
    companion object {
        private const val HTTP_UNAUTHORIZED = 401
        private const val HTTP_TOO_MANY_REQUESTS = 429
    }

    override suspend fun verifyToken(apiKey: String): AuthVerificationResult = withContext(dispatcher) {
        val response = runCatching { api.verifyToken(apiKey) }
            .getOrElse { throwable ->
                return@withContext AuthVerificationResult.Error(cause = throwable)
            }

        when {
            response.isSuccessful -> handleSuccess(apiKey)
            response.code() == HTTP_UNAUTHORIZED -> AuthVerificationResult.Unauthorized
            response.code() == HTTP_TOO_MANY_REQUESTS -> handleRateLimited(response)
            else -> AuthVerificationResult.Error(statusCode = response.code())
        }
    }

    private fun handleSuccess(apiKey: String): AuthVerificationResult {
        secureStorage.save(SecureStorageKeys.API_KEY, apiKey)
        return AuthVerificationResult.Success
    }

    private fun handleRateLimited(response: Response<*>): AuthVerificationResult {
        val retryAfterHeader = response.headers()["Retry-After"]
        val retryAfterMillis = retryAfterHeader?.toLongOrNull()?.let { TimeUnit.SECONDS.toMillis(it) }
            ?: DEFAULT_RETRY_AFTER_MILLIS
        return AuthVerificationResult.RateLimited(retryAfterMillis)
    }

    override suspend fun logout(): Unit = withContext(dispatcher) {
        secureStorage.remove(SecureStorageKeys.API_KEY)
    }
}
