package com.zafar.umair.rentoutadmin.screens.products

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zafar.umair.rentoutadmin.common.composable.DropdownContextMenu
import com.zafar.umair.rentoutadmin.common.composable.LoadImage
import com.zafar.umair.rentoutadmin.common.ext.contextMenu
import com.zafar.umair.rentoutadmin.model.Product
import com.zafar.umair.rentoutadmin.model.RatingReview

@Composable
fun ProductItem(
    product: Product,
    ratingReview: List<RatingReview>,
    options: List<String>,
    onActionClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth(),
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

//            if (product.flag) {
//                Icon(
//                    painter = painterResource(AppIcon.ic_flag),
//                    tint = DarkOrange,
//                    contentDescription = "Flag"
//                )
//            }
            DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
        }
    }
}

@Composable
fun RatingBar(
    rating: Float
) {
    Log.d("RATING", "$rating")
    val maxRating = 5
    Row {
        for (i in 1..maxRating) {
            Icon(if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder, "Rating")
//            IconButton(onClick = { onRatingChanged(i.toFloat()) }) {
//                if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder
//            }
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

fun getProductRating(productId: String, ratings: List<RatingReview>): Float {
    var num = 0
    var rating = 2.0
    ratings.forEach {
        if (it.productId == productId) {
            num++
            rating += it.rating
        }
    }
    if (num == 0) {
        return 0.toFloat()
    }
    return (rating / num).toFloat()
}


