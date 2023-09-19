@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class
)

package com.zafar.umair.rentoutadmin

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zafar.umair.rentoutadmin.common.composable.PermissionDialog
import com.zafar.umair.rentoutadmin.common.composable.RationaleDialog
import com.zafar.umair.rentoutadmin.common.snackbar.SnackbarManager
import com.zafar.umair.rentoutadmin.screens.login.LoginScreen
import com.zafar.umair.rentoutadmin.screens.settings.SettingsScreen
import com.zafar.umair.rentoutadmin.screens.splash.SplashScreen
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.zafar.umair.rentoutadmin.screens.rent_requests.RentRequestItem
import com.zafar.umair.rentoutadmin.screens.rent_requests.RentRequestsScreen
import com.zafar.umair.rentoutadmin.screens.product_details.ProductDetailsScreen
import com.zafar.umair.rentoutadmin.screens.products.ProductsScreen
import com.zafar.umair.rentoutadmin.ui.theme.RentOutTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun RentOutAdminApp() {
    RentOutTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData,
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                },
//                scaffoldState = appState.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    rentOutGraph(appState)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
//    val permissionState =
//        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    permissionState.permissions.forEach { permis ->
        when (permis.permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                if (!permis.status.isGranted) {
                    if (permis.status.shouldShowRationale) RationaleDialog(
                        R.string.location_permission_title,
                        R.string.location_permission_settings
                    )
                    else PermissionDialog(
                        R.string.location_permission_title,
                        R.string.location_permission_description
                    ) { permis.launchPermissionRequest() }
                }
            }

            Manifest.permission.POST_NOTIFICATIONS -> {
                if (!permis.status.isGranted) {
                    if (permis.status.shouldShowRationale) RationaleDialog(
                        R.string.notification_permission_title,
                        R.string.notification_permission_settings
                    )
                    else PermissionDialog(
                        R.string.notification_permission_title,
                        R.string.notification_permission_description
                    ) { permis.launchPermissionRequest() }
                }
            }
        }

    }

}

@Composable
fun rememberAppState(
//    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) =
//    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
    remember(navController, snackbarManager, resources, coroutineScope) {
        RentOutAdminAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            resources = resources,
            snackbarManager = snackbarManager,
            snackbarHostState = snackbarHostState
        )
//        RentOutAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun NavGraphBuilder.rentOutGraph(appState: RentOutAdminAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(PRODUCTS_SCREEN) { ProductsScreen(openScreen = { route -> appState.navigate(route) }) }

    composable(RENT_REQUESTS_SCREEN) {
        RentRequestsScreen(openScreen = { route ->
            appState.navigate(
                route
            )
        })
    }
}
