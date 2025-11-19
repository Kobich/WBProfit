package com.wbprofit.feature.analytics.impl.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wbprofit.feature.analytics.api.entity.AnalyticsDate
import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.feature.analytics.impl.domain.model.SaleRecord
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnalyticsInteractorTest {

    @Test
    fun aggregatedReportGroupsSalesByCatalogItem() = runBlocking {
        val params = SalesParams(
            dateFrom = AnalyticsDate("2025-01-01"),
            flag = 0,
        )
        val interactor = AnalyticsInteractor(
            repository = FakeAnalyticsRepository(
                records = listOf(
                    SaleRecord(
                        nmId = 1_001L,
                        supplierArticle = "ART-01",
                        subject = "Sneakers",
                        brand = "BrandX",
                        techSize = "42",
                        totalPrice = 100.0,
                        paymentSaleAmount = 70.0,
                        forPay = 60.0,
                        isReturn = false,
                    ),
                    SaleRecord(
                        nmId = 1_001L,
                        supplierArticle = "ART-01",
                        subject = "Sneakers",
                        brand = "BrandX",
                        techSize = "42",
                        totalPrice = 120.0,
                        paymentSaleAmount = 90.0,
                        forPay = 85.0,
                        isReturn = false,
                    ),
                    SaleRecord(
                        nmId = 1_001L,
                        supplierArticle = "ART-01",
                        subject = "Sneakers",
                        brand = "BrandX",
                        techSize = "42",
                        totalPrice = -80.0,
                        paymentSaleAmount = -50.0,
                        forPay = -45.0,
                        isReturn = true,
                    ),
                    SaleRecord(
                        nmId = 2_002L,
                        supplierArticle = "ART-02",
                        subject = "Boots",
                        brand = "BrandY",
                        techSize = "43",
                        totalPrice = 200.0,
                        paymentSaleAmount = 200.0,
                        forPay = 190.0,
                        isReturn = false,
                    ),
                ),
            ),
        )

        val report = interactor.getSalesReport(params)

        report.items.forEachIndexed { index, item ->
            println(
                "Aggregated item #$index -> nmId=${item.nmId}, article=${item.supplierArticle}, " +
                    "size=${item.techSize}, subject=${item.subject}, brand=${item.brand}, " +
                    "quantity=${item.quantity}, returns=${item.returns}, operations=${item.operations}, " +
                    "buyout=${item.buyoutPercent}, gross=${item.grossRevenue}, " +
                    "net=${item.netRevenue}, payout=${item.payout}",
            )
        }

        assertEquals(params, report.params)
        assertEquals(2, report.items.size)

        val topItem = report.items[0]
        assertEquals(2_002L, topItem.nmId)
        assertEquals(1, topItem.quantity)
        assertEquals(1, topItem.operations)
        assertEquals(0, topItem.returns)
        assertEquals(100.0, topItem.buyoutPercent, 0.001)
        assertEquals(200.0, topItem.grossRevenue, 0.001)
        assertEquals(200.0, topItem.netRevenue, 0.001)
        assertEquals(190.0, topItem.payout, 0.001)

        val groupedItem = report.items[1]
        assertEquals(1_001L, groupedItem.nmId)
        assertEquals("ART-01", groupedItem.supplierArticle)
        assertEquals("Sneakers", groupedItem.subject)
        assertEquals("BrandX", groupedItem.brand)
        assertEquals("42", groupedItem.techSize)
        assertEquals(2, groupedItem.quantity)
        assertEquals(3, groupedItem.operations)
        assertEquals(1, groupedItem.returns)
        assertEquals((2.0 / 3.0) * 100.0, groupedItem.buyoutPercent, 0.001)
        assertEquals(140.0, groupedItem.grossRevenue, 0.001)
        assertEquals(110.0, groupedItem.netRevenue, 0.001)
        assertEquals(100.0, groupedItem.payout, 0.001)
        assertTrue(
            "Items must be sorted by net revenue desc",
            report.items[0].netRevenue >= report.items[1].netRevenue,
        )
    }

    @Test
    fun emptyReportWhenRepositoryReturnsNoSales() = runBlocking {
        val params = SalesParams(
            dateFrom = AnalyticsDate("2025-01-01"),
            flag = 1,
        )
        val interactor = AnalyticsInteractor(
            repository = FakeAnalyticsRepository(records = emptyList()),
        )

        val report = interactor.getSalesReport(params)

        println("Aggregated items: ${report.items}")

        assertEquals(params, report.params)
        assertTrue(report.items.isEmpty())
    }

    private class FakeAnalyticsRepository(
        private val records: List<SaleRecord>,
    ) : AnalyticsRepository {
        override suspend fun getSales(params: SalesParams): List<SaleRecord> = records
    }
}
