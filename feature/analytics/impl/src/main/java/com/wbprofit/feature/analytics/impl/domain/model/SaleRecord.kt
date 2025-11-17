package com.wbprofit.feature.analytics.impl.domain.model

internal data class SaleRecord(
    val nmId: Long,
    val supplierArticle: String,
    val subject: String,
    val brand: String,
    val techSize: String,
    val totalPrice: Double,
    val paymentSaleAmount: Double,
    val forPay: Double,
    val isReturn: Boolean,
)

internal data class SaleAggregate(
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

internal data class SaleAggregateKey(
    val nmId: Long,
    val supplierArticle: String,
    val techSize: String,
)
