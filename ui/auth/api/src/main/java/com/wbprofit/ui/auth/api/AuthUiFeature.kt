package com.wbprofit.ui.auth.api

import androidx.compose.runtime.Composable

interface AuthUiFeature {
    /**
     * Renders the auth flow.
     *
     * @param onAuthSuccess invoked once authorization is complete so the host can change screens.
     */
    @Composable
    fun Content(onAuthSuccess: () -> Unit)
}
