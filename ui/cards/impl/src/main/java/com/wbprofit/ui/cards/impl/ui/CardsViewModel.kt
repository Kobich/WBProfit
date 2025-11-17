package com.wbprofit.ui.cards.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wbprofit.feature.auth.api.AuthFeature
import com.wbprofit.ui.cards.impl.domain.CardsInteractor
import com.wbprofit.ui.cards.impl.domain.entity.CardsScreenState
import com.wbprofit.ui.cards.impl.ui.entity.CardViewState
import com.wbprofit.ui.cards.impl.ui.entity.CardsScreenViewState
import com.wbprofit.ui.cards.impl.ui.entity.CardsViewState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardsViewModel(
    private val interactor: CardsInteractor,
    private val authFeature: AuthFeature,
) : ViewModel() {
    val uiState: StateFlow<CardsScreenViewState> = interactor.state
        .map { it.map() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CardsScreenViewState.Loading,
        )

    init {
        interactor.init()
    }

    fun refresh() {
        interactor.refresh()
    }

    fun logout() {
        viewModelScope.launch {
            authFeature.logout()
        }
    }

    private fun CardsScreenState.map(): CardsScreenViewState = when (this) {
        is CardsScreenState.Loading -> CardsScreenViewState.Loading
        is CardsScreenState.Error -> CardsScreenViewState.Error(message)
        is CardsScreenState.Success -> CardsScreenViewState.Content(
            CardsViewState(
                cards = cardsState.cards.map { card ->
                    CardViewState(
                        nmId = card.nmId,
                        imtID = card.imtID,
                        title = card.title.orEmpty(),
                        imageUrl = card.imageUrl,
                    )
                },
            ),
        )
    }
}
