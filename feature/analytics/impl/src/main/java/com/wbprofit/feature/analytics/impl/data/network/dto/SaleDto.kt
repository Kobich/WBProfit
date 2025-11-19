package com.wbprofit.feature.analytics.impl.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaleDto(
    @param:Json(name = "date") val date: String,
    @param:Json(name = "lastChangeDate") val lastChangeDate: String,
    @param:Json(name = "warehouseName") val warehouseName: String,
    @param:Json(name = "warehouseType") val warehouseType: String,
    @param:Json(name = "countryName") val countryName: String,
    @param:Json(name = "oblastOkrugName") val oblastOkrugName: String,
    @param:Json(name = "regionName") val regionName: String,
    @param:Json(name = "supplierArticle") val supplierArticle: String,
    @param:Json(name = "nmId") val nmId: Long,
    @param:Json(name = "barcode") val barcode: String,
    @param:Json(name = "category") val category: String,
    @param:Json(name = "subject") val subject: String,
    @param:Json(name = "brand") val brand: String,
    @param:Json(name = "techSize") val techSize: String,
    @param:Json(name = "incomeID") val incomeId: Long,
    @param:Json(name = "isSupply") val isSupply: Boolean,
    @param:Json(name = "isRealization") val isRealization: Boolean,
    @param:Json(name = "totalPrice") val totalPrice: Double,
    @param:Json(name = "discountPercent") val discountPercent: Int,
    @param:Json(name = "spp") val spp: Int,
    @param:Json(name = "paymentSaleAmount") val paymentSaleAmount: Double,
    @param:Json(name = "forPay") val forPay: Double,
    @param:Json(name = "finishedPrice") val finishedPrice: Double,
    @param:Json(name = "priceWithDisc") val priceWithDisc: Double,
    @param:Json(name = "saleID") val saleId: String,
    @param:Json(name = "sticker") val sticker: String,
    @param:Json(name = "gNumber") val gNumber: String,
    @param:Json(name = "srid") val srid: String,
)
