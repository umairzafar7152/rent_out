package com.zafar.umair.rentoutadmin.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.zafar.umair.rentoutadmin.SPLASH_SCREEN
import com.zafar.umair.rentoutadmin.PRODUCTS_SCREEN
import com.zafar.umair.rentoutadmin.model.service.AccountService
import com.zafar.umair.rentoutadmin.model.service.ConfigurationService
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import com.google.firebase.auth.FirebaseAuthException
import com.zafar.umair.rentoutadmin.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : RentOutAdminViewModel(logService) {
    val showError = mutableStateOf(false)

    init {
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser) openAndPopUp(PRODUCTS_SCREEN, SPLASH_SCREEN)
        else openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
//        else createAnonymousAccount(openAndPopUp)
    }

//    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
//        launchCatching(snackbar = false) {
//            try {
//                if(!accountService.hasUser)
//                    accountService.createAnonymousAccount()
//            } catch (ex: FirebaseAuthException) {
//                showError.value = true
//                throw ex
//            }
//            openAndPopUp(PRODUCTS_SCREEN, SPLASH_SCREEN)
//        }
//    }
}
