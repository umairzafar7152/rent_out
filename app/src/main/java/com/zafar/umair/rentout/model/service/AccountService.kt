package com.zafar.umair.rentout.model.service

import com.zafar.umair.rentout.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
  val currentUserId: String
  val hasUser: Boolean

  val currentUser: Flow<User>
  val isAnonymous: Boolean

  suspend fun authenticate(email: String, password: String)
  suspend fun sendRecoveryEmail(email: String)
  suspend fun createAnonymousAccount()
  suspend fun linkAccount(email: String, password: String)
  suspend fun deleteAccount()
  suspend fun signOut()
}
