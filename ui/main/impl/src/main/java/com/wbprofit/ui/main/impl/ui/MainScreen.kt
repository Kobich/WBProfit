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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
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
    var selectedTab: TabItem by remember { mutableStateOf(TabItem.Catalog) }
    val catalogBackStack = rememberNavBackStack(CatalogNavRoute)
    val analyticsBackStack = rememberNavBackStack(AnalyticsNavRoute)
    val saveableStateHolder = rememberSaveableStateHolder()
    val currentBackStack = when (selectedTab) {
        TabItem.Catalog -> catalogBackStack
        TabItem.Analytics -> analyticsBackStack
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (selectedTab == tab) {
                                when (tab) {
                                    TabItem.Catalog -> catalogBackStack.setRoot(CatalogNavRoute)
                                    TabItem.Analytics -> analyticsBackStack.setRoot(AnalyticsNavRoute)
                                }
                            } else {
                                selectedTab = tab
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
            backStack = currentBackStack,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onBack = {
                if (currentBackStack.size > 1) {
                    currentBackStack.removeLastOrNull()
                }
            },
            entryDecorators = listOf(
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = { route ->
                when (route) {
                    CatalogNavRoute -> NavEntry(route) {
                        saveableStateHolder.SaveableStateProvider(TabItem.Catalog.route) {
                            cardsUiFeature.Content(
                                onCardClick = { nmId ->
                                    catalogBackStack.add(CardDetailsNavRoute(nmId))
                                },
                                onLogout = onLogout,
                            )
                        }
                    }

                    AnalyticsNavRoute -> NavEntry(route) {
                        saveableStateHolder.SaveableStateProvider(TabItem.Analytics.route) {
                            analyticsUiFeature.Content()
                        }
                    }

                    is CardDetailsNavRoute -> NavEntry(route) {
                        cardDetailsUiFeature.Content(
                            nmId = route.nmId,
                            onBackClick = {
                                catalogBackStack.removeLastOrNull()
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
