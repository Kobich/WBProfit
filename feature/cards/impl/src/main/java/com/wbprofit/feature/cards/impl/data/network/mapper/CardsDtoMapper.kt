package com.wbprofit.feature.cards.impl.data.network.mapper

import com.wbprofit.feature.cards.api.entity.Card
import com.wbprofit.feature.cards.api.entity.CardCharacteristic
import com.wbprofit.feature.cards.api.entity.CardDetail
import com.wbprofit.feature.cards.api.entity.CardDimensions
import com.wbprofit.feature.cards.impl.data.network.dto.CardDto
import com.wbprofit.feature.cards.impl.data.network.dto.CardsResponseDto
import com.wbprofit.feature.cards.impl.data.network.dto.CursorResultDto
import com.wbprofit.feature.cards.impl.domain.CardsResult
import com.wbprofit.feature.cards.impl.domain.CursorResult

internal class CardsDtoMapper {
    fun map(cardsResponseDto: CardsResponseDto): CardsResult = CardsResult(
        cards = cardsResponseDto.cards.map(::mapCard),
        cursor = mapCursor(cardsResponseDto.cursor),
    )

    fun mapCard(cardDto: CardDto): Card = Card(
        nmId = cardDto.nmID,
        imtID = cardDto.imtID,
        title = cardDto.title ?: cardDto.vendorCode,
        imageUrl = extractPrimaryImage(cardDto),
    )

    fun mapDetail(cardDto: CardDto): CardDetail = CardDetail(
        nmId = cardDto.nmID,
        title = cardDto.title ?: cardDto.vendorCode ?: cardDto.nmID.toString(),
        vendorCode = cardDto.vendorCode,
        brand = cardDto.brand,
        description = cardDto.description,
        photos = extractAllImages(cardDto),
        dimensions = cardDto.dimensions?.let {
            CardDimensions(
                length = it.length,
                width = it.width,
                height = it.height,
                weightBrutto = it.weightBrutto,
                isValid = it.isValid,
            )
        },
        characteristics = cardDto.characteristics.mapNotNull { characteristicDto ->
            val values = parseCharacteristicValue(characteristicDto.value)
            val name = characteristicDto.name
            if (name.isNullOrBlank() || values.isEmpty()) {
                null
            } else {
                CardCharacteristic(name = name, values = values)
            }
        },
        createdAt = cardDto.createdAt,
        updatedAt = cardDto.updatedAt,
    )

    private fun mapCursor(cursorResultDto: CursorResultDto): CursorResult = CursorResult(
        nmID = cursorResultDto.nmID,
        total = cursorResultDto.total,
    )

    private fun extractPrimaryImage(cardDto: CardDto): String? = cardDto.photos.firstOrNull()?.let { photo ->
        photo.c516x688
            ?: photo.hq
            ?: photo.c246x328
            ?: photo.big
            ?: photo.square
            ?: photo.tm
    }

    private fun extractAllImages(cardDto: CardDto): List<String> = cardDto.photos
        .flatMap { photo ->
            listOfNotNull(
                photo.c516x688,
                photo.hq,
                photo.big,
                photo.c246x328,
                photo.square,
                photo.tm,
            )
        }.distinct()

    private fun parseCharacteristicValue(value: Any?): List<String> = when (value) {
        null -> emptyList()
        is List<*> -> value.mapNotNull { entry -> entry?.toString()?.trim()?.takeIf { it.isNotEmpty() } }
        is Number -> listOf(value.toString())
        is Boolean -> listOf(value.toString())
        else ->
            value
                .toString()
                .trim()
                .takeIf { it.isNotEmpty() }
                ?.let { listOf(it) } ?: emptyList()
    }
}
