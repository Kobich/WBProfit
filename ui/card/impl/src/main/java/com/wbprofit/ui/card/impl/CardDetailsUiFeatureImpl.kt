package com.wbprofit.ui.card.impl

import androidx.compose.runtime.Composable
import com.wbprofit.ui.card.api.CardDetailsUiFeature
import com.wbprofit.ui.card.impl.ui.CardDetailsScreen

internal class CardDetailsUiFeatureImpl : CardDetailsUiFeature {
    @Composable
    override fun Content(
        nmId: Long,
        onBackClick: () -> Unit,
    ) {
        CardDetailsScreen(
            nmId = nmId,
            onBackClick = onBackClick,
        )
    }
}
