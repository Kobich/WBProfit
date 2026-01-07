package com.wbprofit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.arttttt.nav3router.Nav3Host
import com.arttttt.nav3router.Router
import com.arttttt.nav3router.rememberNav3Navigator
import com.wbprofit.di.RootRouterQualifier
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
    private val rootRouter by inject<Router<NavKey>>(qualifier = RootRouterQualifier)

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
                    router = rootRouter,
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
    router: Router<NavKey>,
) {
    val backStack = rememberNavBackStack(startDestination)
    val activity = LocalActivity.current
    val navigator = rememberNav3Navigator(
        backStack = backStack,
        onBack = {
            activity?.finish()
        },
    )
    Nav3Host(
        backStack = backStack,
        router = router,
        navigator = navigator,
    ) { stack, onBack, navRouter ->
        NavDisplay(
            backStack = stack,
            onBack = onBack,
            entryProvider = { route ->
                when (route) {
                    AuthNavRoute -> NavEntry(route) {
                        authUiFeature.Content(
                            onAuthSuccess = {
                                navRouter.replaceStack(MainNavRoute)
                            },
                        )
                    }

                    MainNavRoute -> NavEntry(route) {
                        mainUiFeature.Content(
                            onLogout = {
                                navRouter.replaceStack(AuthNavRoute)
                            },
                        )
                    }

                    else -> error("Unknown route: $route")
                }
            },
        )
    }
}
