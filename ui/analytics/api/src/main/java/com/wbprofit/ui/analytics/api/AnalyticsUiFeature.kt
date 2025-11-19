package com.wbprofit.ui.analytics.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface AnalyticsUiFeature {
    @Composable
    fun Content(
        navController: NavHostController,
    )
}
