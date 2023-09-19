package com.zafar.umair.rentoutadmin.screens.login

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.zafar.umair.rentoutadmin.LOGIN_SCREEN
import com.zafar.umair.rentoutadmin.PRODUCTS_SCREEN
import com.zafar.umair.rentoutadmin.R.string as AppText
import com.zafar.umair.rentoutadmin.common.ext.isValidEmail
import com.zafar.umair.rentoutadmin.common.snackbar.SnackbarManager
import com.zafar.umair.rentoutadmin.model.User
import com.zafar.umair.rentoutadmin.model.service.AccountService
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val accountService: AccountService,
  private val storageService: StorageService,
  logService: LogService
) : RentOutAdminViewModel(logService) {
  var uiState = mutableStateOf(LoginUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (password.isBlank()) {
      SnackbarManager.showMessage(AppText.empty_password_error)
      return
    }
    if(email == "admin@rentout.com" && password == "Abc123") {

      launchCatching {
        var token = ""
        accountService.authenticate(email, password).runCatching {
          val currentUser = FirebaseAuth.getInstance().currentUser
          if (currentUser != null && !currentUser.isAnonymous) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
              if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
              }
              token = task.result
            }).await()
          }
          storageService.getUser(currentUser!!.uid).runCatching {
            if (this == null) {
              storageService.saveUser(
                User().copy(
                  id = currentUser.uid,
                  fcmToken = token,
                  email = currentUser.email!!,
                  isAnonymous = false
                )
              )
            } else {
//            val fcmToken = this.fcmToken
              if (fcmToken != token) {
//              fcmToken.add(token)
                FirebaseFirestore.getInstance()
                  .collection("users")
                  .document(currentUser.uid)
                  .update("fcmToken", fcmToken).await()
              } else
                return@runCatching
            }
          }
        }
        openAndPopUp(PRODUCTS_SCREEN, LOGIN_SCREEN)
      }
    } else {
      SnackbarManager.showMessage(AppText.credential_error)
    }
  }

  fun onForgotPasswordClick() {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    launchCatching {
      accountService.sendRecoveryEmail(email)
      SnackbarManager.showMessage(AppText.recovery_email_sent)
    }
  }
}
