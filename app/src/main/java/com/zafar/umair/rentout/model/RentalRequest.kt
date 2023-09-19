package com.zafar.umair.rentout.model

import com.google.firebase.firestore.DocumentId

data class RentalRequest (
    @DocumentId val id: String = "",
    val productId: String = "",
    val userId: String = "",
    val ownerId: String = "",
    val requestTime: String = "",
    val approvalTime: String = "",
    val requestStatus: String = RequestStatusOption.Pending.title,
    val rentalStartDate: String = "",
    val rentalEndDate: String = "",
    val ratingId: String = ""
)
