package com.wbprofit.ui.analytics.impl.ui.entity

internal sealed class AnalyticsScreenViewState {
    data class Loading(
        val date: String,
    ) : AnalyticsScreenViewState()

    data class Content(
        val date: String,
        val periodLabel: String,
        val items: List<AnalyticsItemViewState>,
    ) : AnalyticsScreenViewState()

    data class Error(
        val date: String,
        val message: String,
    ) : AnalyticsScreenViewState()
}

internal data class AnalyticsItemViewState(
    val nmId: Long,
    val supplierArticle: String,
    val subject: String,
    val brand: String,
    val techSize: String,
    val soldCount: Int,
    val returnCount: Int,
    val operationsCount: Int,
    val buyoutPercent: Double,
    val grossRevenue: Double,
    val netRevenue: Double,
    val payout: Double,
)
