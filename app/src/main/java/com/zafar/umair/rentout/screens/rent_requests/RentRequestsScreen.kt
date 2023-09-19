package com.zafar.umair.rentout.screens.rent_requests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zafar.umair.rentout.R
import com.zafar.umair.rentout.common.composable.BasicToolbar
import com.zafar.umair.rentout.common.composable.EmptyListText

@Composable
fun RentRequestsScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RentRequestsViewModel = hiltViewModel()
) {
    val myRequests = viewModel.requests.collectAsStateWithLifecycle(emptyList())
    val myProducts = viewModel.myProducts.collectAsStateWithLifecycle(emptyList())
    val ratingReview = viewModel.ratingReviews.collectAsStateWithLifecycle(emptyList())
//    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
//            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(R.string.rent_requests)
        if (myRequests.value.isEmpty()) {
            EmptyListText()
        } else {
//            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK)
            LazyColumn {
                items(myRequests.value.size, key = { it }) { requestItem ->
//                    val rentalStartDate = sdf.parse(myRequests.value[requestItem].rentalStartDate)
//                    if(System.currentTimeMillis() < rentalStartDate!!.time) {
                    val prod = myProducts.value.find { it.id == myRequests.value[requestItem].productId }
                    if(prod == null) {
                        EmptyListText()
                    } else {
                        OthersRequestItem(
                            openScreen = {
                                openScreen(it)
                            },
                            request = myRequests.value[requestItem],
                            product = prod,
                            ratingReview = ratingReview.value
//                    ratingReview = ratingReview.value.find { it.productId == myRequests.value[requestItem].productId }!!
//              onCheckChange = { viewModel.onTaskCheckChange(tasks.value[requestItem]) },
                        )
                    }
//                    }
                }
            }
        }
    }
}