package com.wbprofit.di

import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Router
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RootRouterQualifier: Qualifier = named("root-router")

val appNavigationModule = module {
    single<Router<NavKey>>(RootRouterQualifier) { Router() }
}
