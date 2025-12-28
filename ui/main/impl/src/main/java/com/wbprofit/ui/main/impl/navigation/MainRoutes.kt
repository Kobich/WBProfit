package com.wbprofit.ui.main.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface MainScreenRoute : NavKey

@Serializable
internal object CatalogNavRoute : MainScreenRoute

@Serializable
internal object AnalyticsNavRoute : MainScreenRoute

@Serializable
internal data class CardDetailsNavRoute(val nmId: Long) : MainScreenRoute
