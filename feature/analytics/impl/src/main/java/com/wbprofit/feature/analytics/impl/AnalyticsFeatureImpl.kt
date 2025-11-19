package com.wbprofit.feature.analytics.impl

import com.wbprofit.feature.analytics.api.AnalyticsFeature
import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.feature.analytics.api.entity.SalesReport
import com.wbprofit.feature.analytics.impl.domain.AnalyticsInteractor

internal class AnalyticsFeatureImpl(
    private val interactor: AnalyticsInteractor,
) : AnalyticsFeature {
    override suspend fun getSalesReport(params: SalesParams): SalesReport =
        interactor.getSalesReport(params)
}
