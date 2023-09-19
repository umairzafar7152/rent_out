package com.zafar.umair.rentout.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class TextMessage(
    @DocumentId val id: String = "",
    val text: String = "",
    val time: Date = Date(),
    val senderId: String = "",
    val recipientId: String = "",
    val senderName: String = ""
)
