package com.wbprofit.feature.analytics.impl.domain

import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.feature.analytics.impl.domain.model.SaleRecord

internal interface AnalyticsRepository {
    suspend fun getSales(params: SalesParams): List<SaleRecord>
}
