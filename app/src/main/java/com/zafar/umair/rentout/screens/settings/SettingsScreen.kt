package com.zafar.umair.rentout.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.common.composable.*
import com.zafar.umair.rentout.common.ext.card
import com.zafar.umair.rentout.common.ext.roundText
import com.zafar.umair.rentout.common.ext.spacer

@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState(initial = null)
    val myRequests = viewModel.requests.collectAsStateWithLifecycle(emptyList())
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
                viewModel.onLoginClick(openScreen)
            }

            RegularCardEditor(
                AppText.create_account,
                AppIcon.ic_create_account,
                "",
                Modifier.card()
            ) {
                viewModel.onSignUpClick(openScreen)
            }
        } else {
            Text("Welcome back ${user?.name}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.roundText(MaterialTheme.colorScheme.onSurface), color = Color.White)
            Spacer(modifier = Modifier.spacer())
            MyProductsCard { viewModel.onMyProductsClick(openAndPopUp) }
            OthersRequestCard {
                viewModel.updateRentRequestStatus(myRequests.value)
                viewModel.onOthersRequestsClick(openAndPopUp)
            }
            MyChatsCard {
                viewModel.onMyChatsClick(openAndPopUp)
            }
            SignOutCard { viewModel.onSignOutClick(restartApp) }
            DeleteMyAccountCard { viewModel.onDeleteMyAccountClick(restartApp) }
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
private fun MyChatsCard(openMyChats: () -> Unit) {
    RegularCardEditor(AppText.my_chats, AppIcon.baseline_chat, "", Modifier.card()) {
        openMyChats()
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

@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        AppText.delete_my_account,
        AppIcon.ic_delete_my_account,
        "",
        Modifier.card()
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_account_title)) },
            text = { Text(stringResource(AppText.delete_account_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete_my_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}
