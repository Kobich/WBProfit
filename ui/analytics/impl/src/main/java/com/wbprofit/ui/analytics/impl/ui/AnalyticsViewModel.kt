package com.wbprofit.ui.analytics.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wbprofit.feature.analytics.api.entity.AnalyticsDate
import com.wbprofit.ui.analytics.impl.domain.AnalyticsInteractor
import com.wbprofit.ui.analytics.impl.ui.entity.AnalyticsScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

internal class AnalyticsViewModel(
    private val interactor: AnalyticsInteractor,
    private val converter: AnalyticsViewStateConverter,
) : ViewModel() {
    private val dateMutableStateFlow = MutableStateFlow("")
    private val dateInput: StateFlow<String> = dateMutableStateFlow.asStateFlow()
    private val dateRegex = Regex("\\d{4}-\\d{2}-\\d{2}")

    private val _dateError = MutableStateFlow<String?>(null)
    val dateError: StateFlow<String?> = _dateError.asStateFlow()

    val uiState: StateFlow<AnalyticsScreenViewState> = combine(
        interactor.state,
        dateInput,
    ) { state, date ->
        converter.convert(state, date)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AnalyticsScreenViewState.Loading(date = ""),
    )

    init {
        interactor.init()
    }

    fun refresh() {
        interactor.refresh()
    }

    fun onDateChanged(value: String) {
        val trimmed = value.trim()
        val date = runCatching { if (trimmed.matches(dateRegex)) trimmed else null }
            .getOrNull()
            ?.let { AnalyticsDate(it) }

        if (date == null) {
            _dateError.value = DATE_ERROR_MESSAGE
        } else {
            _dateError.value = null
            dateMutableStateFlow.value = date.value
        }
        dateMutableStateFlow.value = value
        _dateError.value = null
    }

    fun applyDateFilter() {
        interactor.refreshForDate(AnalyticsDate(dateInput.value))
    }

    private companion object {
        const val DATE_ERROR_MESSAGE = "Используйте формат yyyy-MM-dd"
    }
}
