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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wbprofit.ui.analytics.api.AnalyticsUiFeature
import com.wbprofit.ui.card.api.CardDetailsUiFeature
import com.wbprofit.ui.cards.api.CardsUiFeature
import com.wbprofit.ui.main.impl.ui.entity.TabItem

@Composable
internal fun MainScreen(
    cardsUiFeature: CardsUiFeature,
    cardDetailsUiFeature: CardDetailsUiFeature,
    analyticsUiFeature: AnalyticsUiFeature,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    val tabs = TabItem.items
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val previousRoute = navController.previousBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val isSelected = currentRoute == tab.route ||
                        (currentRoute?.startsWith("card/") == true && previousRoute == tab.route)
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute == tab.route) return@NavigationBarItem
                            navController.navigate(tab.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
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
        NavHost(
            navController = navController,
            startDestination = TabItem.Catalog.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            composable(TabItem.Catalog.route) {
                cardsUiFeature.Content(navController, onLogout)
            }
            composable(TabItem.Analytics.route) {
                analyticsUiFeature.Content(navController)
            }
            composable(
                route = "card/{nmId}",
                arguments = listOf(navArgument("nmId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val nmId = backStackEntry.arguments?.getLong("nmId") ?: return@composable
                cardDetailsUiFeature.Content(navController, nmId)
            }
        }
    }
}
