@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class
)

package com.zafar.umair.rentout

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
import com.zafar.umair.rentout.common.composable.PermissionDialog
import com.zafar.umair.rentout.common.composable.RationaleDialog
import com.zafar.umair.rentout.common.snackbar.SnackbarManager
import com.zafar.umair.rentout.screens.edit_product.EditProductScreen
import com.zafar.umair.rentout.screens.login.LoginScreen
import com.zafar.umair.rentout.screens.settings.SettingsScreen
import com.zafar.umair.rentout.screens.sign_up.SignUpScreen
import com.zafar.umair.rentout.screens.splash.SplashScreen
import com.zafar.umair.rentout.screens.products.ProductsScreen
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.zafar.umair.rentout.screens.chats.AllChatsScreen
import com.zafar.umair.rentout.screens.chats.MyChatScreen
import com.zafar.umair.rentout.screens.my_products.MyProductsScreen
import com.zafar.umair.rentout.screens.product_details.ProductDetailsScreen
import com.zafar.umair.rentout.screens.rent_requests.RentRequestItem
import com.zafar.umair.rentout.screens.rent_requests.RentRequestsScreen
import com.zafar.umair.rentout.theme.RentOutTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun RentOutApp() {
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
        RentOutAppState(
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

fun NavGraphBuilder.rentOutGraph(appState: RentOutAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) },
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(PRODUCTS_SCREEN) { ProductsScreen(openScreen = { route -> appState.navigate(route) }) }

    composable(RENT_REQUESTS_SCREEN) {
        RentRequestsScreen(openScreen = { route ->
            appState.navigate(
                route
            )
        })
    }

    composable(
        route = "$PRODUCT_DETAILS_SCREEN$PRODUCT_ID_ARG",
        arguments = listOf(navArgument(PRODUCT_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        ProductDetailsScreen(
            popUpScreen = { appState.popUp() }
        )
    }

    composable(MY_PRODUCTS_SCREEN) {
        MyProductsScreen(openScreen = { route ->
            appState.navigate(
                route
            )
        })
    }

    composable(ALL_CHATS_SCREEN) {
        AllChatsScreen(openScreen = { route ->
            appState.navigate(
                route
            )
        })
    }

    composable(
        route = "$MY_CHAT_SCREEN$CHANNEL_ID_ARG",
        arguments = listOf(navArgument(CHANNEL_ID) {
            nullable = true
            defaultValue = null
        }),
        deepLinks = listOf(navDeepLink { uriPattern = "$MY_CHAT_SCREEN$EXTERNAL_ID_ARG" })
    ) {
        MyChatScreen(
            popUpScreen = { appState.popUp() }
        )
    }

    composable(
        route = "$RENT_REQUEST_ITEM_SCREEN$REQ_ITEM_ARG",
        arguments = listOf(navArgument(PRODUCT_ID) {
            nullable = true
            defaultValue = null
        }, navArgument(REQUEST_ID) {
            nullable = true
            defaultValue = null
        }),
        deepLinks = listOf(navDeepLink { uriPattern = "$RENT_REQUEST_ITEM_SCREEN$REQ_ITEM_ARG" })
    ) {
        RentRequestItem(
            popUpScreen = { appState.popUp() }
        )
    }

    composable(
        route = "$EDIT_MY_PRODUCT_SCREEN$PRODUCT_ID_ARG",
        arguments = listOf(navArgument(PRODUCT_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        EditProductScreen(
            popUpScreen = { appState.popUp() }
        )
    }
}
