package com.zafar.umair.rentout.screens.sign_up

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.SETTINGS_SCREEN
import com.zafar.umair.rentout.SIGN_UP_SCREEN
import com.zafar.umair.rentout.common.ext.isValidEmail
import com.zafar.umair.rentout.common.ext.isValidPassword
import com.zafar.umair.rentout.common.ext.passwordMatches
import com.zafar.umair.rentout.common.snackbar.SnackbarManager
import com.zafar.umair.rentout.model.User
import com.zafar.umair.rentout.model.service.AccountService
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService
) : RentOutViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onAddressChange(newValue: String) {
        uiState.value = uiState.value.copy(address = newValue)
    }

    fun onPhoneChange(newValue: String) {
        uiState.value = uiState.value.copy(phone = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

        launchCatching {
            var token = ""
            var user1 = User()

//            val user = user1.copy(email = user1.email, fcmToken = token, id = currentUser.uid, isAnonymous = false)
            accountService.linkAccount(email, password).runCatching {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null && !currentUser.isAnonymous) {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(
                                ContentValues.TAG,
                                "Fetching FCM registration token failed",
                                task.exception
                            )
                            return@OnCompleteListener
                        }
                        token = task.result
                    }).await()
                    user1 = user1.copy(
                        fcmToken = token,
                        isAnonymous = false,
                        email = currentUser.email!!,
                        name = uiState.value.name,
                        phone = uiState.value.phone,
                        address = uiState.value.address
                    )

                    Log.d(ContentValues.TAG, "Token is: $token")
                    Log.d(ContentValues.TAG, "User is: ${user1.id} + ${user1.email}")
                }

                storageService.saveUser(user1)
            }
            openAndPopUp(SETTINGS_SCREEN, SIGN_UP_SCREEN)
        }
    }
}
