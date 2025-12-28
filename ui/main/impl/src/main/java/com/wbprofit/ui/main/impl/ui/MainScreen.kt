package com.wbprofit.ui.main.impl.ui

import androidx.activity.compose.BackHandler
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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.arttttt.nav3router.Nav3Host
import com.arttttt.nav3router.Router
import com.wbprofit.ui.analytics.api.AnalyticsUiFeature
import com.wbprofit.ui.card.api.CardDetailsUiFeature
import com.wbprofit.ui.cards.api.CardsUiFeature
import com.wbprofit.ui.main.impl.navigation.AnalyticsNavRoute
import com.wbprofit.ui.main.impl.navigation.CardDetailsNavRoute
import com.wbprofit.ui.main.impl.navigation.CatalogNavRoute
import com.wbprofit.ui.main.impl.navigation.MainScreenRoute
import com.wbprofit.ui.main.impl.ui.entity.TabItem

@Composable
internal fun MainScreen(
    cardsUiFeature: CardsUiFeature,
    cardDetailsUiFeature: CardDetailsUiFeature,
    analyticsUiFeature: AnalyticsUiFeature,
    router: Router<MainScreenRoute>,
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

    val canNavigateUp = currentBackStack.size > 1

    BackHandler(enabled = canNavigateUp) {
        router.pop()
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
                                    TabItem.Catalog -> router.replaceStack(CatalogNavRoute)
                                    TabItem.Analytics -> router.replaceStack(AnalyticsNavRoute)
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
        Nav3Host(
            backStack = currentBackStack,
            router = router,
        ) { backStack, onBack, navRouter ->
            NavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                onBack = onBack,
                entryDecorators = listOf(
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = { route ->
                    when (route) {
                        CatalogNavRoute -> NavEntry(route) {
                            saveableStateHolder.SaveableStateProvider(TabItem.Catalog.route) {
                                cardsUiFeature.Content(
                                    onCardClick = { nmId ->
                                        navRouter.push(CardDetailsNavRoute(nmId))
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
                                onBackClick = navRouter::pop,
                            )
                        }

                        else -> error("Unknown route: $route")
                    }
                },
            )
        }
    }
}
