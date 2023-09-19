package com.zafar.umair.rentoutadmin.screens.products

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentoutadmin.common.composable.EmptyListText
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RatingReview
import com.zafar.umair.rentoutadmin.model.User

@Composable
fun AllProductsTab(
    products: State<List<Product>>,
    users: State<List<User>>,
    ratingReview: State<List<RatingReview>>,
    options: List<String>,
    openScreen: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    if (products.value.isEmpty()) {
        EmptyListText()
    } else {
        ItemTable(items = products.value, users = users.value)
//        LazyColumn {
//            items(products.value.size, key = { it }) { productItem ->
//                ProductItem(
//                    product = products.value[productItem],
//                    ratingReview = ratingReview.value,
//                    options = options,
////              onCheckChange = { viewModel.onTaskCheckChange(tasks.value[productItem]) },
//                    onActionClick = { action ->
//                        viewModel.onProductActionClick(
//                            context,
//                            openScreen,
//                            products.value[productItem],
//                            action
//                        )
//                    }
//                )
//            }
//        }
    }
}

@Composable
fun ItemTable(items: List<Product>, users: List<User>) {
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
            Text(text = "Category", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
            Text(text = "Owner", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
            Text(text = "Title", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
            Text(text = "Description", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
            Text(text = "Price", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
            Text(text = "Cost Per", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
//            Text(text = "Posted Since", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
        }

        // Table Data
        LazyColumn(modifier = Modifier
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
                    val userEmail = users.find { it.id == items[item].userId }?.email
                    Text(text = items[item].category, overflow = TextOverflow.Clip, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
                    Text(text = userEmail!!, overflow = TextOverflow.Clip, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
                    Text(text = items[item].title, overflow = TextOverflow.Clip, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
                    Text(text = items[item].description, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
                    Text(text = "£${items[item].cost}", overflow = TextOverflow.Clip, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
                    Text(text = items[item].costBy, overflow = TextOverflow.Clip, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
//                    Text(text = items[item].postingDateTime, overflow = TextOverflow.Clip, maxLines = 1, modifier = Modifier.width(120.dp).padding(horizontal = 2.dp), textAlign = TextAlign.Center)
                }
            }
        }
    }
}