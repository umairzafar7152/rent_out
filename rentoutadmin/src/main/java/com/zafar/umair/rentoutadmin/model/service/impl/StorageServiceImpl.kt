package com.zafar.umair.rentoutadmin.model.service.impl

import com.google.firebase.firestore.FieldValue
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.service.AccountService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.perf.ktx.trace
import com.zafar.umair.rentoutadmin.model.RatingReview
import com.zafar.umair.rentoutadmin.model.RentalRequest
import com.zafar.umair.rentoutadmin.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
    StorageService {

//    @OptIn(ExperimentalCoroutinesApi::class)
//    override val chatChannels: Flow<List<ChatChannel>>
//        get() =
//            auth.currentUser.flatMapLatest { user ->
//                firestore.collection(PRODUCT_COLLECTION).whereNotEqualTo(USER_ID_FIELD, user.id)
//                    .dataObjects()
//            }

//    override val chatMessages: Flow<List<TextMessage>>
//        get() =
//            firestore.collection("chatChannels").document(channelId)
//                .collection("messages")
//                .orderBy("time").dataObjects()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val products: Flow<List<Product>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(PRODUCT_COLLECTION).whereNotEqualTo(USER_ID_FIELD, user.id)
                    .dataObjects()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val myRentalRequests: Flow<List<RentalRequest>>
        get() =
            auth.currentUser.flatMapLatest {
                firestore.collection(RENTAL_COLLECTION)
                    .dataObjects()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val ratingReviews: Flow<List<RatingReview>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(REVIEW_RATING_COLLECTION)
                    .whereNotEqualTo(RATED_USER_ID_FIELD, user.id)
                    .dataObjects()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val myProducts: Flow<List<Product>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(PRODUCT_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id)
                    .dataObjects()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val user: Flow<User?>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(USER_COLLECTION).document(user.id).dataObjects()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatUsers: Flow<List<User>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(USER_COLLECTION).whereNotEqualTo(USER_ID, user.id)
                    .dataObjects()
            }

    override suspend fun removeProductFromFavorites(productId: String) {
        trace(REMOVE_FAVORITE_TRACE) {
            firestore.collection(FAVORITE_COLLECTION)
                .document(auth.currentUserId)
                .update("productIds", FieldValue.arrayRemove(productId))
        }
    }

    override suspend fun sendRentRequest(rentalRequest: RentalRequest) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK)
        val currentDateAndTime = sdf.format(Date())
        firestore.collection(RENTAL_COLLECTION).document()
            .set(rentalRequest.copy(userId = auth.currentUserId, requestTime = currentDateAndTime))
    }

    override suspend fun getProduct(productId: String): Product? =
        firestore.collection(PRODUCT_COLLECTION).document(productId).get().await().toObject()

    override suspend fun getRequest(reqId: String): RentalRequest? =
        firestore.collection(RENTAL_COLLECTION).document(reqId).get().await().toObject()

    override suspend fun save(product: Product): String =
        trace(SAVE_PRODUCT_TRACE) {
            val productWithUserId = product.copy(userId = auth.currentUserId)
            firestore.collection(PRODUCT_COLLECTION).add(productWithUserId).await().id
        }

    override suspend fun update(product: Product): Unit =
        trace(UPDATE_PRODUCT_TRACE) {
            firestore.collection(PRODUCT_COLLECTION).document(product.id).set(product).await()
        }

    override suspend fun updateRequest(request: RentalRequest): Unit =
        trace(UPDATE_REQUEST_TRACE) {
            firestore.collection(RENTAL_COLLECTION).document(request.id).set(request).await()
        }

    override suspend fun delete(productId: String) {
        firestore.collection(PRODUCT_COLLECTION).document(productId).delete().await()
    }

    override suspend fun getUser(userId: String): User? =
        firestore.collection(USER_COLLECTION).document(userId).get().await().toObject()

    override suspend fun saveUser(user: User): String =
        trace(SAVE_USER_TRACE) {
            val userWithId = user.copy(id = auth.currentUserId)
            firestore.collection(USER_COLLECTION).document(userWithId.id).set(userWithId).toString()
//      firestore.collection(USER_COLLECTION).add(userWithId).await().id
        }

    override suspend fun updateUser(user: User): Unit =
        trace(UPDATE_USER_TRACE) {
            firestore.collection(USER_COLLECTION).document(user.id).set(user).await()
        }

    override suspend fun deleteUser(user: User) {
        firestore.collection(USER_COLLECTION).document(user.id).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val OWNER_ID_FIELD = "ownerId"
        private const val RATED_USER_ID_FIELD = "ratedUserId"
        private const val USER_ID = "id"
        private const val MESSAGES = "messages"
        private const val CHANNEL_ID = "channelId"
        private const val CHAT_CHANNELS = "chatChannels"
        private const val ENGAGED_CHAT_CHANNELS = "engagedChatChannels"
        private const val PRODUCT_COLLECTION = "products"
        private const val RENTAL_COLLECTION = "rentalRequests"
        private const val REVIEW_RATING_COLLECTION = "reviewRatings"
        private const val USER_COLLECTION = "users"
        private const val FAVORITE_COLLECTION = "favoriteProducts"
        private const val SAVE_PRODUCT_TRACE = "saveProduct"
        private const val SAVE_FAVORITE_TRACE = "saveFavorite"
        private const val REMOVE_FAVORITE_TRACE = "removeFavorite"
        private const val SAVE_USER_TRACE = "saveProduct"
        private const val UPDATE_PRODUCT_TRACE = "updateProduct"
        private const val UPDATE_REQUEST_TRACE = "updateRequest"
        private const val UPDATE_USER_TRACE = "updateProduct"
    }
}
