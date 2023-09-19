package com.zafar.umair.rentout.screens.rent_requests

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.zafar.umair.rentout.PRODUCT_ID
import com.zafar.umair.rentout.REQUEST_ID
import com.zafar.umair.rentout.common.ext.idFromParameter
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.model.RequestStatusOption
import com.zafar.umair.rentout.model.User
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RentReqItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
) : RentOutViewModel(logService) {

    private var requestId: String?
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
            storageService.updateRequest(request.value.copy(requestStatus = RequestStatusOption.Approved.title))
        }
    }

    fun onRejectClick() {
        launchCatching {
            storageService.updateRequest(request.value.copy(requestStatus = RequestStatusOption.Rejected.title))
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

//    fun onSendRequestClick(context: Context, rentalRequest: RentalRequest) {
//        launchCatching {
//            storageService.sendRentRequest(rentalRequest).runCatching {
//                Toast.makeText(
//                    context,
//                    "Request sent! Waiting for approval from product owner",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
}
