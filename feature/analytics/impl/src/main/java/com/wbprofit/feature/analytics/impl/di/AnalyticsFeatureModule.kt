package com.wbprofit.feature.analytics.impl.di

import com.wbprofit.core.network.api.NetworkFeature
import com.wbprofit.core.network.api.create
import com.wbprofit.core.network.impl.BuildConfig
import com.wbprofit.feature.analytics.api.AnalyticsFeature
import com.wbprofit.feature.analytics.impl.AnalyticsFeatureImpl
import com.wbprofit.feature.analytics.impl.data.AnalyticsRepositoryImpl
import com.wbprofit.feature.analytics.impl.data.network.AnalyticsApi
import com.wbprofit.feature.analytics.impl.data.network.mapper.SaleDtoMapper
import com.wbprofit.feature.analytics.impl.domain.AnalyticsInteractor
import com.wbprofit.feature.analytics.impl.domain.AnalyticsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val analyticsFeatureModule = module {
    single<AnalyticsFeature> { AnalyticsFeatureImpl(interactor = get()) }

    // domain
    factoryOf(::AnalyticsInteractor)

    // data
    factoryOf(::SaleDtoMapper)
    single<AnalyticsRepository> {
        AnalyticsRepositoryImpl(
            analyticsApi = get(),
            saleDtoMapper = get(),
        )
    }
    single<AnalyticsApi> {
        get<NetworkFeature>().create(
            baseUrl = BuildConfig.WB_ORDERS_API_BASE_URL,
        )
    }
}
