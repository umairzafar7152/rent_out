package com.zafar.umair.rentout.screens.products

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentout.common.composable.EmptyListText
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.RatingReview

@Composable
fun AllProductsTab(
    products: State<List<Product>>,
    ratingReview: State<List<RatingReview>>,
    options: List<String>,
    openScreen: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    if (products.value.isEmpty()) {
        EmptyListText()
    } else {
        LazyColumn {
            items(products.value.size, key = { it }) { productItem ->
                ProductItem(
                    product = products.value[productItem],
                    ratingReview = ratingReview.value,
                    options = options,
//              onCheckChange = { viewModel.onTaskCheckChange(tasks.value[productItem]) },
                    onActionClick = { action ->
                        viewModel.onProductActionClick(
                            context,
                            openScreen,
                            products.value[productItem],
                            action
                        )
                    }
                )
            }
        }
    }
}