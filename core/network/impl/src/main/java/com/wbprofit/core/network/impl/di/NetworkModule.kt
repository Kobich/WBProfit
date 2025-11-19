package com.wbprofit.core.network.impl.di

import com.wbprofit.core.network.api.NetworkFeature
import com.wbprofit.core.network.impl.BuildConfig
import com.wbprofit.core.network.impl.NetworkFeatureImpl
import com.wbprofit.core.network.impl.data.MoshiHolder
import com.wbprofit.core.network.impl.data.OkHttpHolder
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val BASE_URL = "base_url"

val networkFeatureModule = module {
    single<NetworkFeature> {
        NetworkFeatureImpl(
            okHttpHolder = get(),
            moshiHolder = get(),
            defaultBaseUrl = get(named(BASE_URL)),
        )
    }

    // data
    single(named(BASE_URL)) { BuildConfig.WB_API_BASE_URL }
    single { MoshiHolder() }
    single {
        OkHttpHolder(
            secureStorage = get(),
            defaultApiKey = BuildConfig.WB_API_KEY,
        )
    }
}
