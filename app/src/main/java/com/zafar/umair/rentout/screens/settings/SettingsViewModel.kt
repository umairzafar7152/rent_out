package com.zafar.umair.rentout.screens.settings

import android.util.Log
import com.zafar.umair.rentout.ALL_CHATS_SCREEN
import com.zafar.umair.rentout.LOGIN_SCREEN
import com.zafar.umair.rentout.MY_PRODUCTS_SCREEN
import com.zafar.umair.rentout.RENT_REQUESTS_SCREEN
import com.zafar.umair.rentout.SETTINGS_SCREEN
import com.zafar.umair.rentout.SIGN_UP_SCREEN
import com.zafar.umair.rentout.SPLASH_SCREEN
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.model.RequestStatusOption
import com.zafar.umair.rentout.model.service.AccountService
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : RentOutViewModel(logService) {
    val user = storageService.user
    val requests = storageService.othersRentalRequests
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

    fun onMyProductsClick(openScreen: (String, String) -> Unit) =
        openScreen(MY_PRODUCTS_SCREEN, SETTINGS_SCREEN)

    fun onOthersRequestsClick(openScreen: (String, String) -> Unit) = openScreen(
        RENT_REQUESTS_SCREEN, SETTINGS_SCREEN
    )

    fun onMyChatsClick(openScreen: (String, String) -> Unit) =
        openScreen(ALL_CHATS_SCREEN, SETTINGS_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            Log.d("SIGNOUT", "Updated 1")
            deleteFcmToken().runCatching {
                Log.d("SIGNOUT", "Updated 2")
                accountService.signOut()
            }
            Log.d("SIGNOUT", "Updated 3")
            restartApp(SPLASH_SCREEN)
        }
    }

    private suspend fun deleteFcmToken() {
        if (accountService.hasUser && !accountService.isAnonymous) {
            Log.d("SIGNOUT", "deleteFCM 4")
            storageService.updateUserFCM("", accountService.currentUserId)
            Log.d("SIGNOUT", "deleteFCM 5")
//            FirebaseFirestore.getInstance()
//                .collection("users")
//                .document(accountService.currentUserId)
//                .update("fcmToken", "").await()
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(SPLASH_SCREEN)
        }
    }

    fun updateRentRequestStatus(requestList: List<RentalRequest>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = System.currentTimeMillis()
//        myRentalRequests.mapLatest {
//        myRentalRequests.mapLatest {
        requestList.forEach { request ->
            Log.d("STATUS", "Before updating")
            val startDate = dateFormat.parse(request.rentalStartDate)
            val endDate = dateFormat.parse(request.rentalEndDate)
            launchCatching {
                if (currentDate >= startDate!!.time && currentDate <= endDate!!.time && request.requestStatus == RequestStatusOption.Approved.title) {
                    Log.d("STATUS", "updated to ongoing")
                    storageService.updateRequestStatus(
                        RequestStatusOption.Ongoing.title,
                        request.id
                    )
                } else if (currentDate >= startDate.time && currentDate <= endDate!!.time && request.requestStatus == RequestStatusOption.Pending.title) {
                    Log.d("STATUS", "Updated to expired")
                    storageService.updateRequestStatus(
                        RequestStatusOption.Expired.title,
                        request.id
                    )
                } else if (currentDate > endDate!!.time && request.requestStatus == RequestStatusOption.Pending.title) {
                    Log.d("STATUS", "Updated to expired")
                    storageService.updateRequestStatus(
                        RequestStatusOption.Expired.title,
                        request.id
                    )
                } else if (currentDate > endDate.time && request.requestStatus == RequestStatusOption.Approved.title) {
                    Log.d("STATUS", "Updated to rented")
                    storageService.updateRequestStatus(RequestStatusOption.Rented.title, request.id)
                }
            }
        }
//        }
    }
}
