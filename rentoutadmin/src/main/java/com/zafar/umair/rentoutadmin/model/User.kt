package com.zafar.umair.rentoutadmin.model

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val email: String = "",
    val phone: String = "",
//    val name: String = "",
    val address: String = "",
    val fcmToken: String = ""
//    val fcmToken: MutableList<String> = mutableListOf()
)
