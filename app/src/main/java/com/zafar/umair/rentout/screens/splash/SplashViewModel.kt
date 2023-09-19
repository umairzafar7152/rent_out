package com.zafar.umair.rentout.screens.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.zafar.umair.rentout.SPLASH_SCREEN
import com.zafar.umair.rentout.PRODUCTS_SCREEN
import com.zafar.umair.rentout.model.service.AccountService
import com.zafar.umair.rentout.model.service.ConfigurationService
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.screens.RentOutViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : RentOutViewModel(logService) {
    val showError = mutableStateOf(false)

    init {
        Log.d("SPLASH", "INSIDE Init")
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        Log.d("SPLASH", "OnAppStart before if")
        if (accountService.hasUser) openAndPopUp(PRODUCTS_SCREEN, SPLASH_SCREEN)
        else createAnonymousAccount(openAndPopUp)
        Log.d("SPLASH", "OnAppStart after if")
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching(snackbar = false) {
            try {
                if(!accountService.hasUser)
                    accountService.createAnonymousAccount()
                Log.d("SPLASH", "After anonymous")
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            Log.d("SPLASH", "Before open screen")
            openAndPopUp(PRODUCTS_SCREEN, SPLASH_SCREEN)
        }
    }
}
