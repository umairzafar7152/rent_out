package com.zafar.umair.rentout.model

enum class RequestStatusOption(val title: String) {
    Pending("Pending"),
    Approved("Approved"),
    Rejected("Rejected"),
    Expired("Expired"),
    Ongoing("Ongoing"),
    Rented("Rented");
//    DeleteProduct("Delete Product");

    companion object {
        fun getByTitle(title: String): RequestStatusOption {
            values().forEach { action -> if (title == action.title) return action }

            return Pending
        }

        fun getOptions(hasEditOption: Boolean): List<String> {
            val options = mutableListOf<String>()
            values().forEach { taskAction ->
                if (hasEditOption || taskAction != Pending) {
                    options.add(taskAction.title)
                }
            }
            return options
        }
    }
}