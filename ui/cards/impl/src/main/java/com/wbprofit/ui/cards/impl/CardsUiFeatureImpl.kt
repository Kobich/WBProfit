package com.wbprofit.ui.cards.impl

import androidx.compose.runtime.Composable
import com.wbprofit.ui.cards.api.CardsUiFeature
import com.wbprofit.ui.cards.impl.ui.CardsScreen

internal class CardsUiFeatureImpl : CardsUiFeature {
    @Composable
    override fun Content(
        onCardClick: (Long) -> Unit,
        onLogout: () -> Unit,
    ) {
        CardsScreen(
            onCardClick = onCardClick,
            onLogout = onLogout,
        )
    }
}
