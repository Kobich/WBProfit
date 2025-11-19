package com.wbprofit.ui.cards.impl.domain

import com.wbprofit.feature.auth.api.AuthFeature
import com.wbprofit.feature.cards.api.CardsFeature
import com.wbprofit.ui.cards.impl.domain.entity.CardsScreenState
import com.wbprofit.ui.cards.impl.domain.entity.CardsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CardsInteractor(
    private val cardsFeature: CardsFeature,
    private val authFeature: AuthFeature,
) {
    private var job: Job? = null
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main,
    )
    val state: MutableStateFlow<CardsScreenState> =
        MutableStateFlow(CardsScreenState.Loading)

    fun init() {
        refresh()
    }

    fun refresh() {
        job?.cancel()
        job = coroutineScope.launch {
            state.value = CardsScreenState.Loading
            runCatching { cardsFeature.getCards() }
                .onSuccess { cards ->
                    state.value = CardsScreenState.Success(
                        CardsState(cards = cards),
                    )
                }.onFailure { throwable ->
                    Timber.Forest.e(throwable, "Failed to load cards")
                    state.value = CardsScreenState.Error(
                        throwable.message ?: GENERIC_ERROR_MESSAGE,
                    )
                }
        }
    }

    private companion object {
        const val GENERIC_ERROR_MESSAGE = "Unable to load cards"
    }
}
