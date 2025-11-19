package com.wbprofit.ui.auth.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.wbprofit.ui.auth.api.AuthNavRoute
import com.wbprofit.ui.auth.impl.ui.entity.AuthCallbacks
import com.wbprofit.ui.auth.impl.ui.entity.AuthStatus
import com.wbprofit.ui.auth.impl.ui.entity.AuthUiState
import com.wbprofit.ui.main.api.MainNavRoute
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

private const val DEFAULT_RETRY_AFTER_SECONDS = 30L

@Composable
internal fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val callbacks = AuthCallbacks(
        onApiKeyChange = viewModel::onApiKeyChange,
        onContinueClick = viewModel::onContinueClick,
    )

    LaunchedEffect(uiState.status) {
        if (uiState.status == AuthStatus.Success) {
            navController.navigate(MainNavRoute.MAIN) {
                popUpTo(AuthNavRoute.AUTH) { inclusive = true }
            }
        }
    }

    AuthScreenView(
        uiState = uiState,
        callbacks = callbacks,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthScreenView(
    uiState: AuthUiState,
    callbacks: AuthCallbacks,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Авторизация") },
                navigationIcon = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Введите ваш WB API ключ",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "Мы используем ключ, чтобы делать запросы к вашему аккаунту. " +
                    "Вы всегда можете изменить его позже.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            )

            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = callbacks.onApiKeyChange,
                label = { Text(text = "WB API ключ") },
                placeholder = { Text(text = "eyJhbGciOi...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
            )

            StatusMessage(uiState = uiState)

            Button(
                onClick = callbacks.onContinueClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isContinueEnabled,
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(text = "Продолжить")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatusMessage(
    uiState: AuthUiState,
) {
    val status = uiState.status
    val message = when (status) {
        AuthStatus.Idle -> null
        AuthStatus.Success -> "Ключ подтверждён. Можно продолжить."
        is AuthStatus.Error -> status.message
        is AuthStatus.RateLimited -> {
            val seconds = status.retryAfterMillis?.let { millis ->
                TimeUnit.MILLISECONDS.toSeconds(millis.coerceAtLeast(0L)) + 1
            } ?: DEFAULT_RETRY_AFTER_SECONDS
            "Слишком много попыток. Попробуйте через $seconds секунд."
        }
    }

    if (message == null) return

    val color: Color = when (status) {
        AuthStatus.Success -> MaterialTheme.colorScheme.primary
        is AuthStatus.Error -> MaterialTheme.colorScheme.error
        is AuthStatus.RateLimited -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onBackground
    }

    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
    )
}
