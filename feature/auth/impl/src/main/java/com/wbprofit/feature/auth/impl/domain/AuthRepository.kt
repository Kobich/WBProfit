package com.wbprofit.feature.auth.impl.domain

import com.wbprofit.feature.auth.api.AuthVerificationResult

internal interface AuthRepository {
    suspend fun verifyToken(apiKey: String): AuthVerificationResult

    suspend fun logout()
}
