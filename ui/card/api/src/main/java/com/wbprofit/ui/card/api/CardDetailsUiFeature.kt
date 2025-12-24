package com.wbprofit.ui.card.api

import androidx.compose.runtime.Composable

interface CardDetailsUiFeature {
    @Composable
    fun Content(
        nmId: Long,
        onBackClick: () -> Unit,
    )
}
