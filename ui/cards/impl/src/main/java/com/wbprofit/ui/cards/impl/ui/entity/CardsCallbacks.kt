package com.wbprofit.ui.cards.impl.ui.entity

data class CardsCallbacks(
    val onClick: (nmId: Long) -> Unit,
    val onRefresh: () -> Unit,
    // Delete after account3
    val onLogoutClick: () -> Unit,
)
