package com.wbprofit.feature.analytics.impl.data.network

import com.wbprofit.feature.analytics.impl.data.network.dto.SaleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AnalyticsApi {
    @GET("/api/v1/supplier/sales")
    suspend fun getSales(
        @Query("dateFrom") dateFrom: String,
        @Query("flag") flag: Int,
    ): List<SaleDto>
}
