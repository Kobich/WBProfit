package com.wbprofit.feature.auth.api

sealed interface AuthVerificationResult {
    data object Success : AuthVerificationResult

    data object Unauthorized : AuthVerificationResult

    data class RateLimited(val retryAfterMillis: Long?) : AuthVerificationResult

    data class Error(val cause: Throwable? = null, val statusCode: Int? = null) : AuthVerificationResult
}
