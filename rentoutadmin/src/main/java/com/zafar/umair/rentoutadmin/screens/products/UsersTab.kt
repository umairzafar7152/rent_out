package com.zafar.umair.rentoutadmin.screens.products

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.zafar.umair.rentoutadmin.model.RatingReview
import com.zafar.umair.rentoutadmin.model.User

@Composable
fun UsersTab(
    users: State<List<User>>,
//    ratingReview: State<List<RatingReview>>,
//    options: List<String>,
//    openScreen: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    if (users.value.isEmpty()) {
        EmptyListText()
    } else {
        UsersTable(users = users.value)
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
fun UsersTable(users: List<User>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        val scrollState = rememberScrollState()
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Name",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Email",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Phone",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Address",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 2.dp),
                textAlign = TextAlign.Center
            )
        }

        // Table Data
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(users.size) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .border(1.dp, Color.Gray)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Pending",
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = users[item].email,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = users[item].phone,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Address:${users[item].address}",
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