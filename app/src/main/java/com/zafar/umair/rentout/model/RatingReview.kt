package com.zafar.umair.rentout.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class RatingReview(
    @DocumentId val id: String = "",
    val ratedUserId: String = "",
    val ratedUserName: String = "",
    val time: String = "",
    val rating: Double = 0.0,
    val review: String = "",
    val productId: String = ""
)
