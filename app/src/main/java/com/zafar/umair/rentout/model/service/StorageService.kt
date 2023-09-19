package com.zafar.umair.rentout.model.service

import com.zafar.umair.rentout.model.FavoriteDocument
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.RatingReview
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.model.TextMessage
import com.zafar.umair.rentout.model.User
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val favoriteProducts: Flow<List<FavoriteDocument>>
  val othersRentalRequests: Flow<List<RentalRequest>>
  val myRentalRequests: Flow<List<RentalRequest>>
  val products: Flow<List<Product>>
  val ratingReviews: Flow<List<RatingReview>>
  val myProducts: Flow<List<Product>>
  val user: Flow<User?>
  val chatUsers: Flow<List<User>>
//  val chatMessages: Flow<List<TextMessage>>
  suspend fun getRequest(reqId: String): RentalRequest?
  suspend fun getProduct(productId: String): Product?
  suspend fun save(product: Product): String
  suspend fun update(product: Product)
  suspend fun updateRequest(request: RentalRequest)
  suspend fun updateRequestStatus(newStatus: String, reqId: String)
  suspend fun delete(productId: String)
  suspend fun saveUser(user: User): String
  suspend fun getUser(userId: String): User?
  suspend fun updateUser(user: User)
  suspend fun updateUserFCM(token: String, userId: String)
  suspend fun deleteUser(user: User)
  suspend fun saveRatingId(ratingId: String, rentalRequest: RentalRequest)
  suspend fun saveReviewRating(ratingReview: RatingReview): String
  suspend fun sendRentRequest(rentalRequest: RentalRequest)
  suspend fun addProductToFavorites(productId: String)
  suspend fun removeProductFromFavorites(productId: String)
  suspend fun getOrCreateChatChannel(otherUserId: String, onComplete: (channelId: String) -> Unit)
  suspend fun getChatMessages(channelId: String) : Flow<List<TextMessage>>
  suspend fun sendMessage(message: TextMessage, channelId: String)
}
