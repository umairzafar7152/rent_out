package com.zafar.umair.rentoutadmin.screens.product_details

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.zafar.umair.rentoutadmin.common.ext.idFromParameter
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.zafar.umair.rentoutadmin.PRODUCT_ID
import com.zafar.umair.rentoutadmin.model.RentalRequest
import com.zafar.umair.rentoutadmin.model.service.module.FirebaseModule.firebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
) : RentOutAdminViewModel(logService) {
    val product = mutableStateOf(Product())
    private val uriList = mutableListOf("", "", "", "", "", "")

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

    fun onImageChange(index: Int, newUri: String) {
        uriList[index] = newUri
//        product.value = product.value.copy(photos = newValue)
    }

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
        launchCatching { storageService.sendRentRequest(rentalRequest).runCatching {
            Toast.makeText(context, "Request sent! Waiting for approval from product owner", Toast.LENGTH_SHORT).show()
        } }
    }
}

