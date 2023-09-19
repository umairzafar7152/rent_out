
package com.zafar.umair.rentoutadmin.model.service.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
  @Provides fun auth(): FirebaseAuth = Firebase.auth

  @Provides fun firestore(): FirebaseFirestore = Firebase.firestore

  @Provides fun firebaseStorage(): StorageReference = FirebaseStorage.getInstance().reference

  @Provides fun firebaseStorageInstance(): FirebaseStorage = FirebaseStorage.getInstance()
}
