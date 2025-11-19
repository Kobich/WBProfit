package com.wbprofit.feature.auth.api

interface AuthFeature {
    suspend fun verifyToken(apiKey: String): AuthVerificationResult

    suspend fun logout()
}
