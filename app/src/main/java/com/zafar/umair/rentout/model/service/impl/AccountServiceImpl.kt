package com.zafar.umair.rentout.model.service.impl

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.zafar.umair.rentout.model.User
import com.zafar.umair.rentout.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.perf.ktx.trace

import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {

  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()

  override val hasUser: Boolean
    get() = auth.currentUser != null

  override val isAnonymous:Boolean
    get() = auth.currentUser!!.isAnonymous

  override val currentUser: Flow<User>
    get() = callbackFlow {
      val listener =
        FirebaseAuth.AuthStateListener { auth ->
          auth.currentUser?.let {
            User(it.uid, it.isAnonymous).let {user ->
              this.trySend(user)
            }
          }
//          this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
        }
      auth.addAuthStateListener(listener)
      awaitClose { auth.removeAuthStateListener(listener) }
    }

  override suspend fun authenticate(email: String, password: String) {
    deleteAccount().runCatching {
      auth.signInWithEmailAndPassword(email, password).await()
    }
  }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun createAnonymousAccount() {
    Log.d("SIGNOUT", "INSIDE AccountService before anonymous")
    auth.signInAnonymously().await()
    Log.d("SIGNOUT", "INSIDE AccountService after anonymous")
  }

  override suspend fun linkAccount(email: String, password: String): Unit =
    trace(LINK_ACCOUNT_TRACE) {
      val credential = EmailAuthProvider.getCredential(email, password)
      auth.currentUser!!.linkWithCredential(credential).await()
    }

  override suspend fun deleteAccount() {
    auth.currentUser!!.delete().await()
  }

  override suspend fun signOut() {
    if (auth.currentUser!!.isAnonymous) {
      Log.d("SIGNOUT", "INSIDE AccountService IsAnonymous")
      auth.currentUser!!.delete()
    }
    Log.d("SIGNOUT", "INSIDE AccountService before signout")
    auth.signOut()
    Log.d("SIGNOUT", "INSIDE AccountService after signout")

    // Sign the user back in anonymously.
    createAnonymousAccount()
  }

  companion object {
    private const val LINK_ACCOUNT_TRACE = "linkAccount"
  }
}
