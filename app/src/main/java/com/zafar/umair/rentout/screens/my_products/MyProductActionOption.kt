package com.zafar.umair.rentout.screens.my_products

enum class MyProductActionOption(val title: String) {
  EditProduct("Edit Product"),
  ToggleFlag("Toggle flag"),
  DeleteProduct("Delete Product");

  companion object {
    fun getByTitle(title: String): MyProductActionOption {
      values().forEach { action -> if (title == action.title) return action }

      return EditProduct
    }

    fun getOptions(hasEditOption: Boolean): List<String> {
      val options = mutableListOf<String>()
      values().forEach { taskAction ->
        if (hasEditOption || taskAction != EditProduct) {
          options.add(taskAction.title)
        }
      }
      return options
    }
  }
}
