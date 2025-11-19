package com.wbprofit.ui.main.impl.di

import com.wbprofit.ui.main.api.MainUiFeature
import com.wbprofit.ui.main.impl.MainUiFeatureImpl
import org.koin.dsl.module

val mainModule = module {
    single<MainUiFeature> {
        MainUiFeatureImpl(
            cardsUiFeature = get(),
            cardDetailsUiFeature = get(),
            analyticsUiFeature = get(),
        )
    }
}
