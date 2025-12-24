package com.wbprofit.ui.cards.api

import androidx.compose.runtime.Composable

interface CardsUiFeature {
    @Composable
    fun Content(
        onCardClick: (Long) -> Unit,
        onLogout: () -> Unit,
    )
}
