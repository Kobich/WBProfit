package com.wbprofit.feature.analytics.api

import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.feature.analytics.api.entity.SalesReport

interface AnalyticsFeature {
    suspend fun getSalesReport(params: SalesParams): SalesReport
}
