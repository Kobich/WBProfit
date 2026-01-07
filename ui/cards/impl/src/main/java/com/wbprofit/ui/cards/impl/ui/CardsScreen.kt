package com.wbprofit.ui.cards.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.wbprofit.ui.cards.impl.ui.entity.CardsCallbacks
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardsScreen(
    onCardClick: (Long) -> Unit,
    onLogout: () -> Unit,
    vm: CardsViewModel = koinViewModel(),
) {
    val uiState = vm.uiState.collectAsState()
    val callbacks = CardsCallbacks(
        onClick = onCardClick,
        onRefresh = { vm.refresh() },
        onLogoutClick = {
            vm.logout()
            onLogout()
        },
    )
    CardsScreenView(uiState.value, callbacks)
}
