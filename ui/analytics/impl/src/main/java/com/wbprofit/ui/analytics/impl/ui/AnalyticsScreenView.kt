package com.wbprofit.ui.analytics.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsCallbacks
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsItemViewState
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsScreenViewState
import java.util.Locale

@Composable
internal fun AnalyticsScreenView(
    uiState: AnalyticsScreenViewState,
    dateError: String?,
    callbacks: AnalyticsCallbacks,
) {
    when (uiState) {
        is AnalyticsScreenViewState.Loading -> LoadingState(
            dateInput = uiState.date,
            dateError = dateError,
            callbacks = callbacks,
        )
        is AnalyticsScreenViewState.Error -> ErrorState(
            message = uiState.message,
            dateInput = uiState.date,
            dateError = dateError,
            onRetry = callbacks.onRefresh,
            onDateChanged = callbacks.onDateChanged,
            onApplyDate = callbacks.onApplyDate,
        )
        is AnalyticsScreenViewState.Content -> AnalyticsContentView(
            state = uiState,
            dateInput = uiState.date,
            dateError = dateError,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun LoadingState(
    dateInput: String,
    dateError: String?,
    callbacks: AnalyticsCallbacks,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        AnalyticsFilters(
            dateInput = dateInput,
            dateError = dateError,
            onDateChanged = callbacks.onDateChanged,
            onApplyDate = callbacks.onApplyDate,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Загружаем аналитику…",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    dateInput: String,
    dateError: String?,
    onRetry: () -> Unit,
    onDateChanged: (String) -> Unit,
    onApplyDate: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnalyticsFilters(
                dateInput = dateInput,
                dateError = dateError,
                onDateChanged = onDateChanged,
                onApplyDate = onApplyDate,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(onClick = onRetry) {
                Text("Повторить")
            }
        }
    }
}

@Composable
private fun AnalyticsContentView(
    state: AnalyticsScreenViewState.Content,
    dateInput: String,
    dateError: String?,
    callbacks: AnalyticsCallbacks,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        AnalyticsFilters(
            dateInput = dateInput,
            dateError = dateError,
            onDateChanged = callbacks.onDateChanged,
            onApplyDate = callbacks.onApplyDate,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Аналитика на ${state.periodLabel}",
                style = MaterialTheme.typography.titleMedium,
            )
            TextButton(onClick = callbacks.onRefresh) {
                Text("Обновить")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.items) { item ->
                AnalyticsItemCard(item = item, onClick = { callbacks.onItemClick(item.nmId) })
            }
        }
    }
}

@Composable
private fun AnalyticsFilters(
    dateInput: String,
    dateError: String?,
    onDateChanged: (String) -> Unit,
    onApplyDate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = dateInput,
            onValueChange = onDateChanged,
            label = { Text("Дата (yyyy-MM-dd)") },
            isError = dateError != null,
            supportingText = {
                if (dateError != null) {
                    Text(dateError)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onApplyDate, modifier = Modifier.align(Alignment.End)) {
            Text("Применить дату")
        }
    }
}

@Composable
private fun AnalyticsItemCard(
    item: AnalyticsItemViewState,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "${item.supplierArticle} • nmId ${item.nmId}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${item.brand} · ${item.subject} · размер ${item.techSize}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Продано: ${item.soldCount}, возвраты: ${item.returnCount}, операций: ${item.operationsCount}",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "Выкуп: ${item.buyoutPercent.formatPercent()}%",
                style = MaterialTheme.typography.bodySmall,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Валовая: ${item.grossRevenue.formatCurrency()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Поступит: ${item.payout.formatCurrency()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = "Нетто: ${item.netRevenue.formatCurrency()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun Double.formatCurrency(): String =
    String.format(Locale.getDefault(), "%,.2f ₽", this)

private fun Double.formatPercent(): String =
    String.format(Locale.getDefault(), "%.1f", this)

@Preview(showBackground = true)
@Composable
private fun PreviewAnalyticsContent() {
    AnalyticsScreenView(
        uiState = AnalyticsScreenViewState.Content(
            date = "2025-01-01",
            periodLabel = "2025-01-01",
            items = listOf(
                AnalyticsItemViewState(
                    nmId = 123456,
                    supplierArticle = "подушкасветлосерая",
                    subject = "Подушки для путешествий",
                    brand = "Stonks",
                    techSize = "0",
                    soldCount = 4,
                    returnCount = 1,
                    operationsCount = 5,
                    buyoutPercent = 80.0,
                    grossRevenue = 2500.0,
                    netRevenue = 1800.0,
                    payout = 1700.0,
                ),
                AnalyticsItemViewState(
                    nmId = 654321,
                    supplierArticle = "подушкатемносиняя",
                    subject = "Подушки для путешествий",
                    brand = "Stonks",
                    techSize = "0",
                    soldCount = 1,
                    returnCount = 1,
                    operationsCount = 2,
                    buyoutPercent = 50.0,
                    grossRevenue = 700.0,
                    netRevenue = 200.0,
                    payout = 180.0,
                ),
            ),
        ),
        dateError = null,
        callbacks = AnalyticsCallbacks(
            onRefresh = {},
            onApplyDate = {},
            onDateChanged = {},
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoading() {
    AnalyticsScreenView(
        uiState = AnalyticsScreenViewState.Loading(date = "2025-01-01"),
        dateError = null,
        callbacks = AnalyticsCallbacks(onRefresh = {}, onApplyDate = {}, onDateChanged = {}),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewError() {
    AnalyticsScreenView(
        uiState = AnalyticsScreenViewState.Error(
            date = "2025-01-01",
            "Не удалось загрузить отчёт",
        ),
        dateError = "Используйте формат yyyy-MM-dd",
        callbacks = AnalyticsCallbacks(onRefresh = {}, onApplyDate = {}, onDateChanged = {}),
    )
}
