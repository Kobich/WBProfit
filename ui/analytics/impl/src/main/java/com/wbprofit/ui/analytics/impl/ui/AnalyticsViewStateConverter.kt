package com.wbprofit.ui.analytics.impl.ui

import com.wbprofit.feature.analytics.api.entity.SalesItem
import com.wbprofit.ui.analytics.impl.domain.entity.AnalyticsScreenState
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsItemViewState
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsScreenViewState

internal class AnalyticsViewStateConverter {
    fun convert(state: AnalyticsScreenState, date: String): AnalyticsScreenViewState = when (state) {
        AnalyticsScreenState.Loading -> AnalyticsScreenViewState.Loading(
            date = date,
        )
        is AnalyticsScreenState.Error -> AnalyticsScreenViewState.Error(
            date = date,
            message = state.message,
        )
        is AnalyticsScreenState.Success -> {
            AnalyticsScreenViewState.Content(
                date = state.report.params.dateFrom.value,
                periodLabel = state.report.params.dateFrom.value,
                items = state.report.items.map(::mapItem),
            )
        }
    }

    private fun mapItem(item: SalesItem): AnalyticsItemViewState = AnalyticsItemViewState(
        nmId = item.nmId,
        supplierArticle = item.supplierArticle,
        subject = item.subject,
        brand = item.brand,
        techSize = item.techSize,
        soldCount = item.quantity,
        returnCount = item.returns,
        operationsCount = item.operations,
        buyoutPercent = item.buyoutPercent,
        grossRevenue = item.grossRevenue,
        netRevenue = item.netRevenue,
        payout = item.payout,
    )
}
