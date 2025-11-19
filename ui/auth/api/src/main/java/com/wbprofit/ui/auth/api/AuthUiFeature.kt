package com.wbprofit.ui.auth.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface AuthUiFeature {
    @Composable
    fun Content(navController: NavHostController)
}
