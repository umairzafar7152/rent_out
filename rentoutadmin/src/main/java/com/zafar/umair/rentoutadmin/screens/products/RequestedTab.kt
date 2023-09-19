package com.zafar.umair.rentoutadmin.screens.products

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zafar.umair.rentoutadmin.common.composable.EmptyListText
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RatingReview
import com.zafar.umair.rentoutadmin.model.RentalRequest
import com.zafar.umair.rentoutadmin.model.User

@Composable
fun RequestedTab(
    myRequests: State<List<RentalRequest>>,
    products: State<List<Product>>,
    users: State<List<User>>,
    ratingReview: State<List<RatingReview>>,
//    viewModel: ProductsViewModel = hiltViewModel()
) {
//    val context = LocalContext.current
    if (myRequests.value.isEmpty()) {
        EmptyListText()
    } else {
        RequestedTabItem(items = myRequests.value, products = products.value, users = users.value)
    }
}


@Composable
fun RequestedTabItem(items: List<RentalRequest>, products: List<Product>, users: List<User>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {

        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Product", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
            Text(
                text = "Owner", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
            Text(
                text = "User", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
            Text(
                text = "Request Status", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
            Text(
                text = "Request Time", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
            Text(
                text = "Rental Start", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
            Text(
                text = "Rental End", fontWeight = FontWeight.Bold, modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp), textAlign = TextAlign.Center
            )
        }

        // Table Data
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(items.size) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .border(1.dp, Color.Gray)
                        .padding(4.dp)
                ) {
                    Text(
                        text = products.find { it.id == items[item].productId }!!.title,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = users.find { it.id == items[item].ownerId }!!.email,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = users.find { it.id == items[item].userId }!!.email,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${items[item].requestStatus}",
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = items[item].requestTime,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = items[item].rentalStartDate,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = items[item].rentalEndDate,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}