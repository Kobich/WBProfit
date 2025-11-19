package com.wbprofit.ui.analytics.impl.ui.entity

data class AnalyticsCallbacks(
    val onRefresh: () -> Unit,
    val onApplyDate: () -> Unit,
    val onDateChanged: (String) -> Unit,
    val onItemClick: (Long) -> Unit = {},
)
