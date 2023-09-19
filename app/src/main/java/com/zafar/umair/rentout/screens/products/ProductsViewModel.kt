package com.zafar.umair.rentout.screens.products

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
import com.zafar.umair.rentout.CHANNEL_ID
import com.zafar.umair.rentout.MY_CHAT_SCREEN
import com.zafar.umair.rentout.PRODUCT_DETAILS_SCREEN
import com.zafar.umair.rentout.SETTINGS_SCREEN
import com.zafar.umair.rentout.PRODUCT_ID
import com.zafar.umair.rentout.RECIPIENT_ID
import com.zafar.umair.rentout.RentOutConstants.ALL_CATEGORY
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.RatingReview
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.model.RequestStatusOption
import com.zafar.umair.rentout.model.service.AccountService
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentOutViewModel(logService) {

    val options = mutableStateOf<List<String>>(listOf())

    val favOptions = mutableStateOf<List<String>>(listOf())

    val products = storageService.products

    val myRentalRequests = storageService.myRentalRequests

    val favList = storageService.favoriteProducts

    val ratingReviews = storageService.ratingReviews

    val filteredProducts = mutableStateOf(products)

    var currentLocation = mutableStateOf(LatLng(0.0, 0.0))

//    init {
//        Log.d("STATUS", "Before init")
//        updateRentRequestStatus()
//    }

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
        favOptions.value = FavActionOption.getOptions()
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
            ProductActionOption.ProductDetails -> {
                if(!accountService.isAnonymous)
                    openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={${product.id}}")
                else
                    Toast.makeText(context, "Login to continue", Toast.LENGTH_SHORT).show()
            }
            ProductActionOption.AddFavorite -> {
                onAddFavoriteClick(context, product.id)
            }
            ProductActionOption.SendRequest -> {
                if(!accountService.isAnonymous)
                    openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={${product.id}}")
                else
                    Toast.makeText(context, "Login to continue", Toast.LENGTH_SHORT).show()
            }
            ProductActionOption.ChatOwner ->  {
                if(!accountService.isAnonymous)
                    onChatClick(product, openScreen)
                else
                    Toast.makeText(context, "Login to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onFavActionClick(
        context: Context,
        openScreen: (String) -> Unit,
        product: Product,
        action: String
    ) {
        when (FavActionOption.getByTitle(action)) {
            FavActionOption.ProductDetails -> {
                if(!accountService.isAnonymous)
                    openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={${product.id}}")
                else
                    Toast.makeText(context, "Login to continue", Toast.LENGTH_SHORT).show()
            }
            FavActionOption.RemoveFavorite -> {
                    onRemoveFavoriteClick(product.id)
            }
            FavActionOption.SendRequest -> {
                if(!accountService.isAnonymous)
                    openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={${product.id}}")
                else
                    Toast.makeText(context, "Login to continue", Toast.LENGTH_SHORT).show()
            }

            FavActionOption.ChatOwner -> {
                if(!accountService.isAnonymous)
                    onChatClick(product, openScreen)
                else
                    Toast.makeText(context, "Login to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onAddFavoriteClick(context: Context, productId: String) {
        launchCatching {
            storageService.addProductToFavorites(productId).runCatching {
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onRemoveFavoriteClick(productId: String) {
        launchCatching {
            storageService.removeProductFromFavorites(productId)
        }
    }

    private fun onChatClick(product: Product, openScreen: (String) -> Unit) {
        launchCatching {
            storageService.getOrCreateChatChannel(product.userId) {
//                channelId = it
                openScreen("$MY_CHAT_SCREEN?$CHANNEL_ID=$it&$RECIPIENT_ID=${product.userId}")
//                launchCatching {
//                    storageService.getChatMessages(it).runCatching {
//                        chatMessages = this
//                    }
//                }
            }
        }
    }

    private fun onSendRequestClick(context: Context, rentalRequest: RentalRequest) {
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

//    private fun onFlagProductClick(product: Product) {
//        launchCatching { storageService.update(product.copy(flag = !product.flag)) }
//    }

    fun clearFilters() {
        filteredProducts.value = products
    }

    fun saveReviewRating(
        ratingReview: RatingReview,
        rentalRequest: RentalRequest,
        function: (String) -> Unit
    ) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val timeNow = MutableStateFlow(Calendar.getInstance())
        launchCatching {
            storageService.saveReviewRating(
                ratingReview.copy(
                    ratedUserId = accountService.currentUserId,
                    time = dateFormat.format(timeNow.value.time).toString()
                )
            ).run {
                storageService.saveRatingId(this, rentalRequest)
                function(this)
            }
        }
    }
}
