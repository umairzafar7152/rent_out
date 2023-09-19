package com.zafar.umair.rentout.screens.products

enum class FavActionOption(val title: String) {
    ProductDetails("Product Details"),
    RemoveFavorite("Remove from Favorites"),
    SendRequest("Request to Rent"),
    ChatOwner("Chat with owner");
//    ToggleFlag("Toggle flag");
//    DeleteProduct("Delete Product");

    companion object {
        fun getByTitle(title: String): FavActionOption {
            values().forEach { action -> if (title == action.title) return action }

            return ProductDetails
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { taskAction ->
//                if (taskAction != ProductDetails) {
                options.add(taskAction.title)
//                }
            }
            return options
        }
    }
}
