package com.wbprofit.ui.auth.impl.ui.entity

internal data class AuthUiState(
    val apiKey: String = "",
    val isLoading: Boolean = false,
    val status: AuthStatus = AuthStatus.Idle,
) {
    val isContinueEnabled: Boolean
        get() = apiKey.isNotBlank() && !isLoading && status !is AuthStatus.RateLimited
}

internal sealed interface AuthStatus {
    data object Idle : AuthStatus

    data object Success : AuthStatus

    data class Error(val message: String) : AuthStatus

    data class RateLimited(val retryAfterMillis: Long?) : AuthStatus
}

internal data class AuthCallbacks(
    val onApiKeyChange: (String) -> Unit,
    val onContinueClick: () -> Unit,
)
