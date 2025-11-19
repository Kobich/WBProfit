package com.wbprofit.feature.cards.api.entity

data class CardDetail(
    val nmId: Long,
    val title: String,
    val vendorCode: String?,
    val brand: String?,
    val description: String?,
    val photos: List<String>,
    val dimensions: CardDimensions?,
    val characteristics: List<CardCharacteristic>,
    val createdAt: String?,
    val updatedAt: String?,
)

data class CardDimensions(
    val length: Int?,
    val width: Int?,
    val height: Int?,
    val weightBrutto: Double?,
    val isValid: Boolean?,
)

data class CardCharacteristic(
    val name: String,
    val values: List<String>,
)
