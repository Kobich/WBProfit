package com.wbprofit.feature.cards.api

import com.wbprofit.feature.cards.api.entity.Card
import com.wbprofit.feature.cards.api.entity.CardDetail

interface CardsFeature {
    suspend fun getCards(): List<Card>

    suspend fun getCard(nmId: Long): CardDetail?
}
