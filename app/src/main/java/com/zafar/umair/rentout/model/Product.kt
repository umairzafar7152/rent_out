package com.zafar.umair.rentout.model

import com.google.firebase.firestore.DocumentId

data class Product(
    @DocumentId val id: String = "",
    val title: String = "",
    val category: String = "",
    val postingDateTime: String = "",
    val description: String = "",
    val cost: Double = 0.0,
    val costBy: String = CostBy.DAY.costDuration,
    val flag: Boolean = false,
    val userId: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val photos: ArrayList<String> = arrayListOf("", "", "", "", "", "")
)

enum class CostBy(val costDuration: String) {
    DAY("DAY"),
    HOUR("HOUR")
}
