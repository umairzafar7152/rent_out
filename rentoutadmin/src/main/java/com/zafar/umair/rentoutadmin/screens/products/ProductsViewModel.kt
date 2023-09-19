package com.zafar.umair.rentoutadmin.screens.products

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.zafar.umair.rentoutadmin.CHANNEL_ID
import com.zafar.umair.rentoutadmin.MY_CHAT_SCREEN
import com.zafar.umair.rentoutadmin.PRODUCT_DETAILS_SCREEN
import com.zafar.umair.rentoutadmin.SETTINGS_SCREEN
import com.zafar.umair.rentoutadmin.PRODUCT_ID
import com.zafar.umair.rentoutadmin.RECIPIENT_ID
import com.zafar.umair.rentoutadmin.RentOutConstants.ALL_CATEGORY
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RentalRequest
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService
) : RentOutAdminViewModel(logService) {

    val options = mutableStateOf<List<String>>(listOf())

    val products = storageService.products

    val myRentalRequests = storageService.myRentalRequests

    val ratingReviews = storageService.ratingReviews

    val filteredProducts = mutableStateOf(products)

    val users = storageService.chatUsers

    var currentLocation = mutableStateOf(LatLng(0.0, 0.0))

    //    init {
//        var token = ""
//        var user = User()
//        val currentUser = FirebaseAuth.getInstance().currentUser!!
//        if(!currentUser.isAnonymous) {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
//                    return@OnCompleteListener
//                }
//                token = task.result
//                user = User().copy(email = currentUser.email!!, fcmToken = token, id = currentUser.uid, isAnonymous = false)
//                Log.d(ContentValues.TAG, "Token is: $token")
//                Log.d(ContentValues.TAG, "User is: ${user.id} + ${user.email}")
//
//            })
//            launchCatching {
//                storageService.getUser(currentUser.uid).runCatching {
//                    if (this == null) {
//                        storageService.saveUser(user)
//                    } else {
//                        FirebaseFirestore.getInstance()
//                            .collection("users")
//                            .document(currentUser.uid)
//                            .update("fcmToken", token).await()
//                    }
//                }
//            }
//        }
//    }

    @SuppressLint("MissingPermission")
    fun getLocation(
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(listener: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener {
                if (it == null) {
                    Log.d("LOCATION", "Cannot get location.")
                    callback(false)
//                    Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT).show()
                } else {
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                    Log.d(
                        "LOCATION",
                        "location is: ${currentLocation.value.latitude} & ${currentLocation.value.longitude}"
                    )
                    callback(true)
                }
            }
    }

    fun loadProductOptions() {
        options.value = ProductActionOption.getOptions()
    }

//  fun onTaskCheckChange(product: Product) {
//    launchCatching { storageService.update(product.copy(address = !product.address)) }
//  }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getFilterList(category: String, price: Double): Flow<List<Product>> {
        if (category != ALL_CATEGORY && price != 0.0) {
            filteredProducts.value = products.mapLatest { list ->
                list.filter { it.category == category }
                list.filter { it.cost <= price }
            }
        } else if (category != ALL_CATEGORY) {
            filteredProducts.value = products.mapLatest { list ->
                list.filter { it.category == category }
            }
        } else if (price != 0.0 && price > 0) {
            filteredProducts.value = products.mapLatest { list ->
                list.filter { it.cost <= price }
            }
        } else {
            filteredProducts.value = products
        }
        return filteredProducts.value
    }
//    fun getFilterList(flowList: Flow<List<Product>>): Flow<List<Product>>{
//        return flowList.mapLatest { list ->
//            list.filter { it.category == "Costumes" }
//        }
//    }

    fun onMapMarkerClick(productId: String, openScreen: (String) -> Unit) {
        openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={$productId}")
    }

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onProductActionClick(
        context: Context,
        openScreen: (String) -> Unit,
        product: Product,
        action: String
    ) {
        when (ProductActionOption.getByTitle(action)) {
            ProductActionOption.ProductDetails -> openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={${product.id}}")
            ProductActionOption.SendRequest -> onSendRequestClick(
                context,
                RentalRequest(productId = product.id, ownerId = product.userId)
            )

        }
    }

    private fun onRemoveFavoriteClick(productId: String) {
        launchCatching {
            storageService.removeProductFromFavorites(productId)
        }
    }

    private fun onSendRequestClick(context: Context, rentalRequest: RentalRequest) {
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

//    private fun onFlagProductClick(product: Product) {
//        launchCatching { storageService.update(product.copy(flag = !product.flag)) }
//    }

    fun clearFilters() {
        filteredProducts.value = products
    }
}
