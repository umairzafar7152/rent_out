package com.zafar.umair.rentout.screens.products

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentout.common.composable.EmptyListText
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.model.RatingReview
import com.zafar.umair.rentout.model.RentalRequest

@Composable
fun RequestedTab(
    myRequests: State<List<RentalRequest>>,
    products: State<List<Product>>,
    ratingReview: State<List<RatingReview>>,
    viewModel: ProductsViewModel = hiltViewModel()
) {
//    val context = LocalContext.current
    if (myRequests.value.isEmpty()) {
        EmptyListText()
    } else {
        LazyColumn {
            items(myRequests.value.size, key = { it }) { productItem ->
                RequestItem(
                    request = myRequests.value[productItem],
                    product = products.value.find { it.id == myRequests.value[productItem].productId }!!,
                    ratingReview = ratingReview.value,
                    viewModel = viewModel
//                    ratingReview = ratingReview.value.find { it.productId == myRequests.value[productItem].productId }!!
//              onCheckChange = { viewModel.onTaskCheckChange(tasks.value[productItem]) },
                )
            }
        }
    }
}