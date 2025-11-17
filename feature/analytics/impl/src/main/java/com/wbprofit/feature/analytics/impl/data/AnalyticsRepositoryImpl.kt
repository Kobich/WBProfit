package com.wbprofit.feature.analytics.impl.data

import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.feature.analytics.impl.data.network.AnalyticsApi
import com.wbprofit.feature.analytics.impl.data.network.mapper.SaleDtoMapper
import com.wbprofit.feature.analytics.impl.domain.AnalyticsRepository
import com.wbprofit.feature.analytics.impl.domain.model.SaleRecord

internal class AnalyticsRepositoryImpl(
    private val analyticsApi: AnalyticsApi,
    private val saleDtoMapper: SaleDtoMapper,
) : AnalyticsRepository {
    override suspend fun getSales(params: SalesParams): List<SaleRecord> {
        val sales = analyticsApi.getSales(
            dateFrom = params.dateFrom.value,
            flag = params.flag,
        )
        return sales.map(saleDtoMapper::map)
    }
}
