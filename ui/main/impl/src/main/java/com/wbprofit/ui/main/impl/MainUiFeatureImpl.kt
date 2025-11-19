package com.wbprofit.ui.main.impl

import androidx.compose.runtime.Composable
import com.wbprofit.ui.analytics.api.AnalyticsUiFeature
import com.wbprofit.ui.card.api.CardDetailsUiFeature
import com.wbprofit.ui.cards.api.CardsUiFeature
import com.wbprofit.ui.main.api.MainUiFeature
import com.wbprofit.ui.main.impl.ui.MainScreen

internal class MainUiFeatureImpl(
    private val cardsUiFeature: CardsUiFeature,
    private val cardDetailsUiFeature: CardDetailsUiFeature,
    private val analyticsUiFeature: AnalyticsUiFeature,
) : MainUiFeature {
    @Composable
    override fun Content(onLogout: () -> Unit) {
        MainScreen(
            cardsUiFeature = cardsUiFeature,
            cardDetailsUiFeature = cardDetailsUiFeature,
            analyticsUiFeature = analyticsUiFeature,
            onLogout = onLogout,
        )
    }
}
