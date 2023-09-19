package com.zafar.umair.rentoutadmin.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentoutadmin.R.drawable as AppIcon
import com.zafar.umair.rentoutadmin.R.string as AppText
import com.zafar.umair.rentoutadmin.common.composable.*
import com.zafar.umair.rentoutadmin.common.ext.card
import com.zafar.umair.rentoutadmin.common.ext.spacer

@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
//    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState(false))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(AppText.settings)

        Spacer(modifier = Modifier.spacer())

        if (uiState.isAnonymousAccount) {
            RegularCardEditor(AppText.sign_in, AppIcon.ic_sign_in, "", Modifier.card()) {
                viewModel.onLoginClick(openAndPopUp)
            }

        } else {
//            MyProductsCard { viewModel.onMyProductsClick(openAndPopUp) }
//            OthersRequestCard { viewModel.onOthersRequestsClick(openAndPopUp) }
            SignOutCard { viewModel.onSignOutClick(restartApp) }
        }
    }
}

@Composable
private fun MyProductsCard(openMyProducts: () -> Unit) {
    RegularCardEditor(AppText.my_products, AppIcon.baseline_product, "", Modifier.card()) {
        openMyProducts()
    }
}

@Composable
private fun OthersRequestCard(openRequest: () -> Unit) {
    RegularCardEditor(AppText.others_requests, AppIcon.baseline_request_page, "", Modifier.card()) {
        openRequest()
    }
}

@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(AppText.sign_out, AppIcon.ic_exit, "", Modifier.card()) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}
