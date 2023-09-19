package com.zafar.umair.rentout.screens.edit_product

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.zafar.umair.rentout.common.ext.idFromParameter
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.zafar.umair.rentout.PRODUCT_ID
import com.zafar.umair.rentout.model.service.module.FirebaseModule.firebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
) : RentOutViewModel(logService) {
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

    fun getUriAt(index: Int): String {
        return uriList[index]
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

    fun onDoneClick(context: Context, popUpScreen: () -> Unit) {
        var loc: LatLng
        launchCatching {
            loc = getLocation(context)
            Log.d("LOCATION", "location is: ${loc.latitude}, ${loc.longitude}")
            product.value = product.value.copy(lat = loc.latitude, lon = loc.longitude)
            launchCatching {
                val editedTask = product.value
                if (editedTask.id.isBlank()) {
                    storageService.save(editedTask)
                } else {
                    storageService.update(editedTask)
                }
                popUpScreen()
            }
        }
    }

    fun uploadImagesToFirebase(coroutineScope: CoroutineScope, callback: () -> Unit) {
        val localPhotosVar = product.value.photos
        coroutineScope.launch {
            uriList.forEachIndexed { index, uri ->
                if (uri != "") {
                    val fileName = UUID.randomUUID().toString() + ".jpg"
                    val refStorage = firebaseStorage().child("images/$fileName")
                    withContext(Dispatchers.IO) {
                        val uploadTask = refStorage.putFile(Uri.parse(uri))
                        val downloadUrl = uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            refStorage.downloadUrl
                        }.await()
                        localPhotosVar[index] = downloadUrl.toString()
                    }
                }
            }
            product.value = product.value.copy(photos = localPhotosVar)
            Log.d("Testing RENT OUT", "after----------${product.value.photos}------------")
            callback()
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(
        context: Context
    ): LatLng {
        val loc: Location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        loc = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(listener: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener {

            }.await()
        val lat: Double = loc.latitude
        val lon = loc.longitude
        return LatLng(lat, lon)
    }
}

//    fun uploadImageToFirebase(fileUri: Uri, callback: (String) -> Unit) {
//        val fileName = UUID.randomUUID().toString() + ".jpg"
//
//        val refStorage = firebaseStorage().child("images/$fileName")
//
//        refStorage.putFile(fileUri)
//            .addOnSuccessListener { taskSnapshot ->
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
//                    val imageUrl = it.toString()
//                    callback(imageUrl)
//                }
//            }.addOnFailureListener { e ->
//                print(e.message)
//            }
//    }
//}
