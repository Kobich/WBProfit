package com.wbprofit.ui.card.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wbprofit.base.ui.R
import com.wbprofit.ui.card.impl.ui.entity.CardDetailsCallbacks
import com.wbprofit.ui.card.impl.ui.entity.CardDetailsUiState
import com.wbprofit.ui.card.impl.ui.entity.CardDetailsViewState
import com.wbprofit.ui.card.impl.ui.entity.CardInfoItem
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

const val CARD_SIZE = 3f / 4f

@Composable
internal fun CardDetailsScreen(
    nmId: Long,
    onBackClick: () -> Unit,
    vm: CardDetailsViewModel = koinViewModel(parameters = { parametersOf(nmId) }),
) {
    val uiState by vm.uiState.collectAsState()

    val callbacks = CardDetailsCallbacks(onBackClick = onBackClick)

    CardDetailsScreenView(
        uiState = uiState,
        callbacks = callbacks,
    )
}

@Composable
internal fun CardDetailsScreenView(
    uiState: CardDetailsUiState,
    callbacks: CardDetailsCallbacks,
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = callbacks.onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_arrow_back_ios_24),
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Text(
                    text = "Карточка",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) { innerPadding ->
        when (uiState) {
            is CardDetailsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Ошибка: ${uiState.message}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            CardDetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Загрузка...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            is CardDetailsUiState.Content -> {
                CardDetailsContent(
                    state = uiState.state,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun CardDetailsContent(
    state: CardDetailsViewState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CardDetailsHeader(state)
        CardDetailsInfo(state.infoItems)
        CardDetailsDescription(state.description)
        CardDetailsCharacteristics(state)
    }
}

@Composable
private fun CardDetailsHeader(
    state: CardDetailsViewState,
) {
    val primaryImageUrl = state.imageUrls.firstOrNull()

    if (primaryImageUrl != null) {
        AsyncImage(
            model = primaryImageUrl,
            placeholder = painterResource(R.drawable.empty_card_icon),
            error = painterResource(R.drawable.empty_card_icon),
            contentDescription = state.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(CARD_SIZE),
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(CARD_SIZE)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.empty_card_icon),
                contentDescription = null,
            )
        }
    }

    Text(
        text = state.title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun CardDetailsInfo(
    infoItems: List<CardInfoItem>,
) {
    if (infoItems.isEmpty()) return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Параметры",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        infoItems.forEach { item ->
            Text(
                text = "${item.title}: ${item.value}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun CardDetailsDescription(
    description: String?,
) {
    val text = description?.takeIf { it.isNotBlank() } ?: return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Описание",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun CardDetailsCharacteristics(
    state: CardDetailsViewState,
) {
    if (state.characteristics.isEmpty()) return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Характеристики",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        state.characteristics.forEach { characteristic ->
            Column {
                Text(
                    text = characteristic.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = characteristic.value,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Clip,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
