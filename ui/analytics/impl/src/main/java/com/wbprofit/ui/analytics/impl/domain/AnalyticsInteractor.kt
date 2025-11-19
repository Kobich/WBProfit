package com.wbprofit.ui.analytics.impl.domain

import com.wbprofit.feature.analytics.api.AnalyticsFeature
import com.wbprofit.feature.analytics.api.entity.AnalyticsDate
import com.wbprofit.feature.analytics.api.entity.SalesParams
import com.wbprofit.ui.analytics.impl.domain.entity.AnalyticsScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal class AnalyticsInteractor(
    private val analyticsFeature: AnalyticsFeature,
) {
    private var job: Job? = null
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var currentParams: SalesParams = defaultParams()
    val state: MutableStateFlow<AnalyticsScreenState> =
        MutableStateFlow(AnalyticsScreenState.Loading)

    fun init() {
        refreshWith(currentParams)
    }

    fun refresh() {
        refreshWith(currentParams)
    }

    fun refreshForDate(date: AnalyticsDate) {
        currentParams = currentParams.copy(dateFrom = date)
        refreshWith(currentParams)
    }

    private fun refreshWith(params: SalesParams) {
        job?.cancel()
        job = coroutineScope.launch {
            state.value = AnalyticsScreenState.Loading
            runCatching { analyticsFeature.getSalesReport(params) }
                .onSuccess { report -> state.value = AnalyticsScreenState.Success(report) }
                .onFailure { throwable ->
                    state.value = AnalyticsScreenState.Error(
                        throwable.message ?: GENERIC_ERROR_MESSAGE,
                    )
                }
        }
    }

    private fun defaultParams(): SalesParams {
        val formatter = SimpleDateFormat(DATE_PATTERN, Locale.US)
        val today = formatter.format(Calendar.getInstance().time)
        return SalesParams(
            dateFrom = AnalyticsDate(today),
            flag = DEFAULT_FLAG,
        )
    }

    private companion object {
        const val DEFAULT_FLAG = 0
        const val GENERIC_ERROR_MESSAGE = "Не удалось загрузить отчёт"
        const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
