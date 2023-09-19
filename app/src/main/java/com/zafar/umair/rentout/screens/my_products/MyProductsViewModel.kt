package com.zafar.umair.rentout.screens.my_products

import androidx.compose.runtime.mutableStateOf
import com.zafar.umair.rentout.EDIT_MY_PRODUCT_SCREEN
import com.zafar.umair.rentout.SETTINGS_SCREEN
import com.zafar.umair.rentout.PRODUCT_ID
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.service.ConfigurationService
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : RentOutViewModel(logService) {
    val options = mutableStateOf<List<String>>(listOf())

    val myProducts = storageService.myProducts

    fun loadProductOptions() {
        val hasEditOption = configurationService.isShowProductEditButtonConfig
        options.value = MyProductActionOption.getOptions(hasEditOption)
    }

//  fun onTaskCheckChange(product: Product) {
//    launchCatching { storageService.update(product.copy(address = !product.address)) }
//  }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_MY_PRODUCT_SCREEN)

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onProductActionClick(openScreen: (String) -> Unit, product: Product, action: String) {
        when (MyProductActionOption.getByTitle(action)) {
            MyProductActionOption.EditProduct -> openScreen("$EDIT_MY_PRODUCT_SCREEN?$PRODUCT_ID={${product.id}}")
            MyProductActionOption.ToggleFlag -> onFlagProductClick(product)
            MyProductActionOption.DeleteProduct -> onDeleteProductClick(product)
        }
    }

    private fun onFlagProductClick(product: Product) {
        launchCatching { storageService.update(product.copy(flag = !product.flag)) }
    }

    private fun onDeleteProductClick(product: Product) {
        launchCatching { storageService.delete(product.id) }
    }
}
