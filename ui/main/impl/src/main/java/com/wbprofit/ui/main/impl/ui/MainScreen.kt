package com.wbprofit.ui.main.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.wbprofit.ui.analytics.api.AnalyticsUiFeature
import com.wbprofit.ui.card.api.CardDetailsUiFeature
import com.wbprofit.ui.cards.api.CardsUiFeature
import com.wbprofit.ui.main.impl.navigation.AnalyticsNavRoute
import com.wbprofit.ui.main.impl.navigation.CardDetailsNavRoute
import com.wbprofit.ui.main.impl.navigation.CatalogNavRoute
import com.wbprofit.ui.main.impl.ui.entity.TabItem

@Composable
internal fun MainScreen(
    cardsUiFeature: CardsUiFeature,
    cardDetailsUiFeature: CardDetailsUiFeature,
    analyticsUiFeature: AnalyticsUiFeature,
    onLogout: () -> Unit,
) {
    val tabs = TabItem.items
    val navStack = rememberNavBackStack(CatalogNavRoute)
    val selectedTab = when (navStack.lastOrNull()) {
        AnalyticsNavRoute -> TabItem.Analytics
        else -> TabItem.Catalog
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            when (tab) {
                                TabItem.Catalog -> navStack.setRoot(CatalogNavRoute)
                                TabItem.Analytics -> navStack.setRoot(AnalyticsNavRoute)
                            }
                        },
                        icon = {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = tab.badgeText,
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        },
                        label = { Text(tab.label) },
                        alwaysShowLabel = true,
                    )
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = navStack,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onBack = {
                if (navStack.size > 1) {
                    navStack.removeLastOrNull()
                }
            },
            entryProvider = { route ->
                when (route) {
                    CatalogNavRoute -> NavEntry(route) {
                        cardsUiFeature.Content(
                            onCardClick = { nmId ->
                                navStack.add(CardDetailsNavRoute(nmId))
                            },
                            onLogout = onLogout,
                        )
                    }

                    AnalyticsNavRoute -> NavEntry(route) {
                        analyticsUiFeature.Content()
                    }

                    is CardDetailsNavRoute -> NavEntry(route) {
                        cardDetailsUiFeature.Content(
                            nmId = route.nmId,
                            onBackClick = {
                                navStack.removeLastOrNull()
                            },
                        )
                    }

                    else -> error("Unknown route: $route")
                }
            },
        )
    }
}

private fun NavBackStack<NavKey>.setRoot(route: NavKey) {
    if (isEmpty()) {
        add(route)
        return
    }

    this[0] = route
    if (size > 1) {
        subList(1, size).clear()
    }
}
