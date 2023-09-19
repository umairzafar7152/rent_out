package com.zafar.umair.rentoutadmin.screens.rent_requests

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.zafar.umair.rentoutadmin.PRODUCT_ID
import com.zafar.umair.rentoutadmin.REQUEST_ID
import com.zafar.umair.rentoutadmin.common.ext.idFromParameter
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RentalRequest
import com.zafar.umair.rentoutadmin.model.User
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RentReqItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
) : RentOutAdminViewModel(logService) {

    var requestId: String?
    var productId: String?

    //    val myProducts = storageService.myProducts
//    val requests = storageService.othersRentalRequests
//    val ratingReviews = storageService.ratingReviews
    val product = mutableStateOf(Product())
    val request = mutableStateOf(RentalRequest())
    val customer = mutableStateOf(User())


    init {
        requestId = savedStateHandle.get<String>(REQUEST_ID)
        productId = savedStateHandle.get<String>(PRODUCT_ID)
        if (productId != null) {
            launchCatching {
                product.value =
                    storageService.getProduct(productId!!.idFromParameter()) ?: Product()
            }
        }
        if (requestId != null) {
            launchCatching {
                storageService.getRequest(requestId!!.idFromParameter()).runCatching {
                    if (this != null)
                        request.value = this
                    else request.value = RentalRequest()
                    customer.value =
                        storageService.getUser(this!!.userId) ?: User()
                }
            }
        }
    }

    fun onApproveClick() {
        launchCatching {
            storageService.updateRequest(request.value.copy(requestStatus = "Approved"))
        }
    }

    fun onRejectClick() {
        launchCatching {
            storageService.updateRequest(request.value.copy(requestStatus = "Rejected"))
        }
    }

//    private val uriList = mutableListOf("", "", "", "", "", "")

    fun onDoneClick(popUpScreen: () -> Unit) {
//        launchCatching {
//            val editedTask = product.value
//            if (editedTask.id.isBlank()) {
//                storageService.save(editedTask)
//            } else {
//                storageService.update(editedTask)
//            }
//        }
        popUpScreen()
    }

    fun onSendRequestClick(context: Context, rentalRequest: RentalRequest) {
        launchCatching {
            storageService.sendRentRequest(rentalRequest).runCatching {
                Toast.makeText(
                    context,
                    "Request sent! Waiting for approval from product owner",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
