package com.wbprofit.ui.analytics.impl.domain.entity

import com.wbprofit.feature.analytics.api.entity.SalesReport

internal sealed interface AnalyticsScreenState {
    data object Loading : AnalyticsScreenState

    data class Success(val report: SalesReport) : AnalyticsScreenState

    data class Error(val message: String) : AnalyticsScreenState
}
