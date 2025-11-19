package com.wbprofit.feature.analytics.impl.data.network.mapper

import com.wbprofit.feature.analytics.impl.data.network.dto.SaleDto
import com.wbprofit.feature.analytics.impl.domain.model.SaleRecord

internal class SaleDtoMapper {
    fun map(dto: SaleDto): SaleRecord = SaleRecord(
        nmId = dto.nmId,
        supplierArticle = dto.supplierArticle,
        subject = dto.subject,
        brand = dto.brand,
        techSize = dto.techSize,
        totalPrice = dto.totalPrice,
        paymentSaleAmount = dto.paymentSaleAmount,
        forPay = dto.forPay,
        isReturn = dto.totalPrice < 0 || dto.saleId.startsWith("R", ignoreCase = true),
    )
}
