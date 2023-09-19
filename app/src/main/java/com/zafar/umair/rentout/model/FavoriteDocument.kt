package com.zafar.umair.rentout.model

data class FavoriteDocument (
    val userId: String = "",
    val productIds: MutableList<String> = mutableListOf()
)