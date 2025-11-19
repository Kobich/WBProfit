package com.wbprofit.ui.analytics.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsCallbacks
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AnalyticsScreen(
    vm: AnalyticsViewModel = koinViewModel(),
) {
    val uiState = vm.uiState.collectAsState()
    val dateError = vm.dateError.collectAsState()
    val callbacks = AnalyticsCallbacks(
        onRefresh = vm::refresh,
        onApplyDate = vm::applyDateFilter,
        onDateChanged = vm::onDateChanged,
    )
    AnalyticsScreenView(
        uiState = uiState.value,
        dateError = dateError.value,
        callbacks = callbacks,
    )
}
