package com.wbprofit.feature.analytics.impl.domain

import com.wbprofit.feature.analytics.api.entity.SalesItem
import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.feature.analytics.api.entity.SalesReport
import com.wbprofit.feature.analytics.impl.domain.model.SaleAggregate
import com.wbprofit.feature.analytics.impl.domain.model.SaleAggregateKey
import com.wbprofit.feature.analytics.impl.domain.model.SaleRecord

internal class AnalyticsInteractor(
    private val repository: AnalyticsRepository,
) {
    companion object {
        private const val PERCENTAGE_MULTIPLIER = 100.0
    }

    suspend fun getSalesReport(params: SalesParams): SalesReport {
        val records = repository.getSales(params)
        if (records.isEmpty()) {
            return SalesReport(params = params, items = emptyList())
        }

        val items = records
            .aggregateByItem()
            .map { aggregate ->
                SalesItem(
                    nmId = aggregate.nmId,
                    supplierArticle = aggregate.supplierArticle,
                    subject = aggregate.subject,
                    brand = aggregate.brand,
                    techSize = aggregate.techSize,
                    quantity = aggregate.soldCount,
                    operations = aggregate.operationsCount,
                    returns = aggregate.returnCount,
                    buyoutPercent = aggregate.buyoutPercent,
                    grossRevenue = aggregate.grossRevenue,
                    netRevenue = aggregate.netRevenue,
                    payout = aggregate.payout,
                )
            }.sortedByDescending(SalesItem::netRevenue)

        return SalesReport(
            params = params,
            items = items,
        )
    }

    // Groups raw sale rows by catalog item (nmId + supplier article + size) and enriches them
    // with counts for sales vs returns plus derived buyout percentage.
    private fun Collection<SaleRecord>.aggregateByItem(): List<SaleAggregate> =
        groupBy { SaleAggregateKey(it.nmId, it.supplierArticle, it.techSize) }
            .map { (key, groupedSales) ->
                val firstSale = groupedSales.first()
                val operationsCount = groupedSales.size
                val returnCount = groupedSales.count(SaleRecord::isReturn)
                val soldCount = operationsCount - returnCount
                val buyoutPercent = if (operationsCount == 0) {
                    0.0
                } else {
                    (soldCount.toDouble() / operationsCount) * PERCENTAGE_MULTIPLIER
                }
                SaleAggregate(
                    nmId = key.nmId,
                    supplierArticle = key.supplierArticle,
                    subject = firstSale.subject,
                    brand = firstSale.brand,
                    techSize = key.techSize,
                    soldCount = soldCount,
                    returnCount = returnCount,
                    operationsCount = operationsCount,
                    buyoutPercent = buyoutPercent,
                    grossRevenue = groupedSales.sumOf(SaleRecord::totalPrice),
                    netRevenue = groupedSales.sumOf(SaleRecord::paymentSaleAmount),
                    payout = groupedSales.sumOf(SaleRecord::forPay),
                )
            }
}
