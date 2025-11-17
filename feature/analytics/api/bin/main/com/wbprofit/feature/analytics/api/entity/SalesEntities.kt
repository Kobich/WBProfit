package com.wbprofit.feature.analytics.api.entity

data class SalesReport(
    val params: SalesParams,
    val items: List<SalesItem>,
)

data class SalesParams(
    val dateFrom: AnalyticsDate,
    val flag: Int,
)

/**
 * Represents date parameter accepted by analytics endpoints.
 * Stored in ISO-8601 (yyyy-MM-dd) format required by WB API.
 */
@JvmInline
value class AnalyticsDate(val value: String) {
    init {
        require(value.matches(DATE_REGEX)) {
            "Date must be in ISO yyyy-MM-dd format, but was: $value"
        }
    }

    companion object {
        private val DATE_REGEX = Regex("\\d{4}-\\d{2}-\\d{2}")
    }
}

/**
 * Aggregated per-item sales metrics.
 *
 * @property quantity число проданных единиц (без возвратов)
 * @property operations количество операций (продажи + возвраты)
 * @property returns количество возвратов
 * @property buyoutPercent доля выкупа (quantity / operations * 100)
 */
data class SalesItem(
    val nmId: Long,
    val supplierArticle: String,
    val subject: String,
    val brand: String,
    val techSize: String,
    val quantity: Int,
    val operations: Int,
    val returns: Int,
    val buyoutPercent: Double,
    val grossRevenue: Double,
    val netRevenue: Double,
    val payout: Double,
)
