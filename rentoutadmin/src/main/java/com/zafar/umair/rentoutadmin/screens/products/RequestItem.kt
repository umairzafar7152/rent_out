package com.zafar.umair.rentoutadmin.screens.products

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zafar.umair.rentoutadmin.PRODUCT_ID
import com.zafar.umair.rentoutadmin.RENT_REQUEST_ITEM_SCREEN
import com.zafar.umair.rentoutadmin.REQUEST_ID
import com.zafar.umair.rentoutadmin.common.composable.LoadImage
import com.zafar.umair.rentoutadmin.common.ext.roundText
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RatingReview
import com.zafar.umair.rentoutadmin.model.RentalRequest

@Composable
fun RequestItem(
    isClickable: Boolean,
    openScreen: (String) -> Unit,
    request: RentalRequest,
    product: Product,
    ratingReview: List<RatingReview>
) {
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 8.dp)
            .clickable {
                if (isClickable) {
                    Log.d("TESTER", "O YEAH. IT WORKED!")
                    openScreen("$RENT_REQUEST_ITEM_SCREEN?$REQUEST_ID={${request.id}}&$PRODUCT_ID={${product.id}}")
                }
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
        ) {
            LoadImage(path = product.photos[0])
//            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            ) {
                Text(text = product.title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "£${product.cost} per ${product.costBy}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${getProductRating(product.id, ratingReview)}")
                    Spacer(modifier = Modifier.width(12.dp))
                    RatingBar(getProductRating(product.id, ratingReview))
                }
            }
            when (request.requestStatus) {
                "Pending" -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                "Approved" -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                "Rejected" -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(Color.Gray)
                        )
                    }
                }
            }
//            Text(
//                text = request.requestStatus,
//                modifier = Modifier.roundText(MaterialTheme.colorScheme.primary),
//                color = Color.White
//            )

//            if (product.flag) {
//                Icon(
//                    painter = painterResource(AppIcon.ic_flag),
//                    tint = DarkOrange,
//                    contentDescription = "Flag"
//                )
//            }
//            DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
        }
    }
}

//@Composable
//fun RatingBar(
//    rating: Float,
//    maxRating: Int = 5,
//    onRatingChanged: (Float) -> Unit
//) {
//    Row {
//        for (i in 1..maxRating) {
//            IconButton(onClick = { onRatingChanged(i.toFloat()) }) {
//                if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder
//            }
//        }
//    }
//}


