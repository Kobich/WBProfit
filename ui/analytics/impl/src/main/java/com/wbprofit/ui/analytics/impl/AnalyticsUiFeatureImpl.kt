package com.wbprofit.ui.analytics.impl

import androidx.compose.runtime.Composable
import com.wbprofit.ui.analytics.api.AnalyticsUiFeature
import com.wbprofit.ui.analytics.impl.ui.AnalyticsScreen

internal class AnalyticsUiFeatureImpl : AnalyticsUiFeature {
    @Composable
    override fun Content() {
        AnalyticsScreen()
    }
}
