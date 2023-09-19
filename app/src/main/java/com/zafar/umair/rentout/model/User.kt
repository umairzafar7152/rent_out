package com.zafar.umair.rentout.model

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val fcmToken: String = ""
//    val fcmToken: MutableList<String> = mutableListOf()
)
