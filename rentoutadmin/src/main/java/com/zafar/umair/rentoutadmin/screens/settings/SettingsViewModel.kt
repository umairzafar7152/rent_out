package com.zafar.umair.rentoutadmin.screens.settings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zafar.umair.rentoutadmin.ALL_CHATS_SCREEN
import com.zafar.umair.rentoutadmin.LOGIN_SCREEN
import com.zafar.umair.rentoutadmin.MY_PRODUCTS_SCREEN
import com.zafar.umair.rentoutadmin.RENT_REQUESTS_SCREEN
import com.zafar.umair.rentoutadmin.SETTINGS_SCREEN
import com.zafar.umair.rentoutadmin.SIGN_UP_SCREEN
import com.zafar.umair.rentoutadmin.SPLASH_SCREEN
import com.zafar.umair.rentoutadmin.model.service.AccountService
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : RentOutAdminViewModel(logService) {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }

    fun onLoginClick(openScreen: (String, String) -> Unit) = openScreen(LOGIN_SCREEN, SETTINGS_SCREEN)

//    fun onMyProductsClick(openScreen: (String, String) -> Unit) = openScreen(MY_PRODUCTS_SCREEN, SETTINGS_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            deleteFcmToken().runCatching {
                accountService.signOut()
            }
            restartApp(SPLASH_SCREEN)
        }
    }

    private suspend fun deleteFcmToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null && !currentUser.isAnonymous) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.uid)
                .update("fcmToken", "").await()
        }
    }

//    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
//        launchCatching {
//            accountService.deleteAccount()
//            restartApp(SPLASH_SCREEN)
//        }
//    }
}
