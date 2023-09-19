package com.zafar.umair.rentoutadmin.model.service

import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RatingReview
import com.zafar.umair.rentoutadmin.model.RentalRequest
import com.zafar.umair.rentoutadmin.model.User
import kotlinx.coroutines.flow.Flow

interface StorageService {
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
  suspend fun delete(productId: String)
  suspend fun saveUser(user: User): String
  suspend fun getUser(userId: String): User?
  suspend fun updateUser(user: User)
  suspend fun deleteUser(user: User)
  suspend fun sendRentRequest(rentalRequest: RentalRequest)
  suspend fun removeProductFromFavorites(productId: String)
}
