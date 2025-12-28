package com.wbprofit.ui.main.impl.di

import com.arttttt.nav3router.Router
import com.wbprofit.ui.main.api.MainUiFeature
import com.wbprofit.ui.main.impl.MainUiFeatureImpl
import com.wbprofit.ui.main.impl.navigation.MainScreenRoute
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val MainScreenRouterQualifier: Qualifier = named("main-screen-router")

val mainModule = module {
    single<Router<MainScreenRoute>>(MainScreenRouterQualifier) { Router() }

    single<MainUiFeature> {
        MainUiFeatureImpl(
            cardsUiFeature = get(),
            cardDetailsUiFeature = get(),
            analyticsUiFeature = get(),
            router = get(qualifier = MainScreenRouterQualifier),
        )
    }
}
