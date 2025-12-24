package com.wbprofit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.wbprofit.base.ui.theme.MovieCatalogTheme
import com.wbprofit.core.keystore.api.KeystoreFeature
import com.wbprofit.core.keystore.api.SecureStorageKeys
import com.wbprofit.ui.auth.api.AuthNavRoute
import com.wbprofit.ui.auth.api.AuthUiFeature
import com.wbprofit.ui.main.api.MainNavRoute
import com.wbprofit.ui.main.api.MainUiFeature
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val mainUiFeature by inject<MainUiFeature>()
    private val authUiFeature by inject<AuthUiFeature>()
    private val secureStorage by inject<KeystoreFeature>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination = if (secureStorage.contains(SecureStorageKeys.API_KEY)) {
            MainNavRoute
        } else {
            AuthNavRoute
        }

        setContent {
            MovieCatalogTheme {
                MainNavHost(
                    startDestination = startDestination,
                    authUiFeature = authUiFeature,
                    mainUiFeature = mainUiFeature,
                )
            }
        }
    }
}

@Composable
private fun MainNavHost(
    startDestination: NavKey,
    authUiFeature: AuthUiFeature,
    mainUiFeature: MainUiFeature,
) {
    val backStack = rememberNavBackStack(startDestination)
    val activity = LocalActivity.current

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            } else {
                activity?.finish()
            }
        },
        entryProvider = { route ->
            when (route) {
                AuthNavRoute -> NavEntry(route) {
                    authUiFeature.Content(
                        onAuthSuccess = {
                            backStack.setRoot(MainNavRoute)
                        },
                    )
                }

                MainNavRoute -> NavEntry(route) {
                    mainUiFeature.Content(
                        onLogout = {
                            backStack.setRoot(AuthNavRoute)
                        },
                    )
                }

                else -> error("Unknown route: $route")
            }
        },
    )
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
