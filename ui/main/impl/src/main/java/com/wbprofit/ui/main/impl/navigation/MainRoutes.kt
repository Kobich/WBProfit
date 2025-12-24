package com.wbprofit.ui.main.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal object CatalogNavRoute : NavKey

@Serializable
internal object AnalyticsNavRoute : NavKey

@Serializable
internal data class CardDetailsNavRoute(val nmId: Long) : NavKey
