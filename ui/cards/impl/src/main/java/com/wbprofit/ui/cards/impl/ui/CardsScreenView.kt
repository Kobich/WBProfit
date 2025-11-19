package com.wbprofit.ui.cards.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wbprofit.base.ui.R
import com.wbprofit.ui.cards.impl.ui.entity.CardViewState
import com.wbprofit.ui.cards.impl.ui.entity.CardsCallbacks
import com.wbprofit.ui.cards.impl.ui.entity.CardsScreenViewState
import com.wbprofit.ui.cards.impl.ui.entity.CardsViewState

@Composable
fun CardsScreenView(
    uiState: CardsScreenViewState,
    callbacks: CardsCallbacks,
) {
    when (uiState) {
        is CardsScreenViewState.Error -> {
            // Center
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Error: ${uiState.message}")
            }
        }

        CardsScreenViewState.Loading -> {
            // Center
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Loading...")
            }
        }

        is CardsScreenViewState.Content -> {
            CardsContentView(
                uiState = uiState.state,
                callbacks = callbacks,
            )
        }
    }
}

@Composable
fun CardsContentView(
    uiState: CardsViewState,
    callbacks: CardsCallbacks,
) {
    val cards = uiState.cards

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
    ) { innerPadding ->
        Column {
            Image(
                painter = painterResource(id = R.drawable.logout_ic),
                contentDescription = "Logout",
                modifier = Modifier
                    .padding(innerPadding)
                    .size(36.dp)
                    .clickable { callbacks.onLogoutClick() },
            )
            CardsCatalogList(
                items = cards,
                onClick = callbacks.onClick,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
fun CardCatalogCard(
    card: CardViewState,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = card.imageUrl,
            placeholder = painterResource(R.drawable.empty_card_icon),
            error = painterResource(R.drawable.empty_card_icon),
            fallback = painterResource(R.drawable.empty_card_icon),
            contentDescription = card.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = card.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
fun CardsCatalogList(
    items: List<CardViewState>,
    onClick: (nmId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
        content = {
            items(items.size) { index ->
                val card = items[index]
                CardCatalogCard(card, onClick = { onClick(card.nmId) })
            }
        },
    )
}

@Preview
@Composable
fun PreviewRenderLoadingScreen() {
    CardsScreenView(
        uiState = CardsScreenViewState.Loading,
        callbacks = CardsCallbacks(
            onClick = {},
            onRefresh = {},
            onLogoutClick = {},
        ),
    )
}

@Preview
@Composable
fun PreviewRenderErrorScreen() {
    CardsScreenView(
        uiState = CardsScreenViewState.Error("Error"),
        callbacks = CardsCallbacks(
            onClick = {},
            onRefresh = {},
            onLogoutClick = {},
        ),
    )
}

@Preview
@Composable
fun PreviewRenderMovieScreen() {
    CardsContentView(
        uiState = CardsViewState(
            cards = listOf(
                CardViewState(nmId = 1, imtID = 1, title = "Карточка 1", imageUrl = null),
                CardViewState(nmId = 2, imtID = 2, title = "Карточка 2", imageUrl = null),
                CardViewState(nmId = 3, imtID = 3, title = "Карточка 3", imageUrl = null),
                CardViewState(nmId = 4, imtID = 4, title = "Карточка 4", imageUrl = null),
            ),
        ),
        callbacks = CardsCallbacks(
            onClick = {},
            onRefresh = {},
            onLogoutClick = {},
        ),
    )
}
