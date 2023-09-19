package com.zafar.umair.rentoutadmin.model.service

import com.zafar.umair.rentoutadmin.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
  val currentUserId: String
  val hasUser: Boolean

  val currentUser: Flow<User>

  suspend fun authenticate(email: String, password: String)
  suspend fun sendRecoveryEmail(email: String)
  suspend fun signOut()
}
