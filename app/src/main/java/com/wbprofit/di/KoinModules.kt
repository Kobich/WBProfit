package com.wbprofit.di

import com.wbprofit.core.keystore.impl.di.keystoreModule
import com.wbprofit.core.network.impl.di.networkFeatureModule
import com.wbprofit.feature.analytics.impl.di.analyticsFeatureModule
import com.wbprofit.feature.auth.impl.di.authFeatureModule
import com.wbprofit.feature.cards.impl.di.cardsFeatureModule
import com.wbprofit.ui.analytics.impl.di.analyticsUiModule
import com.wbprofit.ui.auth.impl.di.authUiModule
import com.wbprofit.ui.card.impl.di.cardDetailsModule
import com.wbprofit.ui.cards.impl.di.cardsModule
import com.wbprofit.ui.main.impl.di.mainModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(
    // Features core
    networkFeatureModule,
    keystoreModule,
    // Features screen
    analyticsFeatureModule,
    authFeatureModule,
    cardsFeatureModule,
    // UI
    cardDetailsModule,
    cardsModule,
    analyticsUiModule,
    mainModule,
    authUiModule,
    // App scoped
    appNavigationModule,
)
