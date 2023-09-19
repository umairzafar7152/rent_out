package com.zafar.umair.rentout.screens.product_details

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.zafar.umair.rentout.common.ext.idFromParameter
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.zafar.umair.rentout.PRODUCT_ID
import com.zafar.umair.rentout.model.RentalRequest
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
) : RentOutViewModel(logService) {
    val ratingReviews = storageService.ratingReviews
    val users = storageService.chatUsers
    val product = mutableStateOf(Product())
//    private val uriList = mutableListOf("", "", "", "", "", "")

    init {
        val productId = savedStateHandle.get<String>(PRODUCT_ID)
        if (productId != null) {
            launchCatching {
                product.value = storageService.getProduct(productId.idFromParameter()) ?: Product()
            }
        }
    }

    fun onTitleChange(newValue: String) {
        product.value = product.value.copy(title = newValue)
    }

    fun onCostChange(newValue: String) {
        product.value = product.value.copy(cost = newValue.toDouble())
    }

    fun onDescriptionChange(newValue: String) {
        product.value = product.value.copy(description = newValue)
    }

    fun onCategoryChange(newValue: String) {
        product.value = product.value.copy(category = newValue)
    }

    fun onDateTimeChange(newValue: String) {
        product.value = product.value.copy(postingDateTime = newValue)
    }

//    fun onImageChange(index: Int, newUri: String) {
//        uriList[index] = newUri
////        product.value = product.value.copy(photos = newValue)
//    }

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
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK)
        if (sdf.parse(rentalRequest.rentalStartDate)!! > sdf.parse(rentalRequest.rentalStartDate)!!) {
            Toast.makeText(context, "Enter valid dates to continue!", Toast.LENGTH_SHORT).show()
        } else {
            launchCatching {
                storageService.sendRentRequest(rentalRequest).runCatching {
                    Toast.makeText(
                        context,
                        "Request sent! Waiting for approval from product owner",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

