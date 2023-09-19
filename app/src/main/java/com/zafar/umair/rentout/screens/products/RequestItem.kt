package com.zafar.umair.rentout.screens.products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.zafar.umair.rentout.common.composable.BasicField
import com.zafar.umair.rentout.common.composable.LoadImage
import com.zafar.umair.rentout.common.composable.ResponseButton
import com.zafar.umair.rentout.common.ext.filledBasicButton
import com.zafar.umair.rentout.common.ext.roundText
import com.zafar.umair.rentout.model.Product
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.model.RatingReview
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.model.RequestStatusOption
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RequestItem(
//    openScreen: (String) -> Unit,
    request: RentalRequest,
    product: Product,
    ratingReview: List<RatingReview>,
    viewModel: ProductsViewModel
) {
    val review = remember {
        mutableStateOf("")
    }
    val rating = remember {
        mutableStateOf(0.0.toFloat())
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 8.dp)
//            .clickable {
//                Log.d("TESTER", "O YEAH. IT WORKED!")
//                openScreen("$RENT_REQUEST_ITEM_SCREEN?$REQUEST_ID={${request.id}}&$PRODUCT_ID={${product.id}}")
//            }
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
                Text(text = product.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "£${product.cost} per ${product.costBy}",
                    style = MaterialTheme.typography.bodySmall
                )
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text("${getProductRating(product.id, ratingReview)}")
//                    Spacer(modifier = Modifier.width(12.dp))
//                    MyRatingBar(getProductRating(product.id, ratingReview))
//                }
                Text(
                    text = "Dated: ${request.requestTime}",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = "Starts: ${request.rentalStartDate}",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = "Ends: ${request.rentalEndDate}",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Clip
                )
            }
            when (request.requestStatus) {
                RequestStatusOption.Pending.title -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                RequestStatusOption.Approved.title -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(Color(0xFF367E3A))
                        )
                    }
                }

                RequestStatusOption.Rejected.title -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(Color(0xFFB92E2E))
                        )
                    }
                }
                RequestStatusOption.Expired.title -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(Color(0xFFB92E2E))
                        )
                    }
                }
                RequestStatusOption.Rented.title -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                RequestStatusOption.Ongoing.title -> {
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Text(
                            request.requestStatus,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(MaterialTheme.colorScheme.primary)
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
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK)
        val date = sdf.parse(request.rentalEndDate)
//        if(request.ratingId!="" && System.currentTimeMillis()>date.time) {
        if (request.ratingId == "" && System.currentTimeMillis() > date!!.time && request.requestStatus == RequestStatusOption.Rented.title) {
            Divider()
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Give rating", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${rating.value}")
                    Spacer(modifier = Modifier.width(12.dp))
                    RatingBar(
                        value = rating.value,
                        config = RatingBarConfig()
                            .style(RatingBarStyle.HighLighted),
                        onValueChange = {
                            rating.value = it
                        },
                        onRatingChanged = {
                            Log.d("TAG", "onRatingChanged: $it")
                        }
                    )
                }
                BasicField(
                    text = AppText.review,
                    value = review.value,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isEnabled = true,
                    onNewValue = {
                        review.value = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ResponseButton(
                    text = AppText.give_feedback,
                    modifier = Modifier.filledBasicButton(MaterialTheme.colorScheme.primary),
                    MaterialTheme.colorScheme.primary
                ) {
                    isLoading.value = true
                    viewModel.saveReviewRating(
                        RatingReview(
                            rating = rating.value.toDouble(),
                            review = review.value,
                            productId = product.id
                        ), request
                    ) {
                        isLoading.value = false
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    if (isLoading.value) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .pointerInput(Unit) {}
        )
        CircularProgressIndicator()
    }
}

//@Composable
//fun MyRatingBar(
//    rating: Float,
//    maxRating: Int = 5,
////    onRatingChanged: (Float) -> Unit
//) {
//    Row {
//        for (i in 1..maxRating) {
////            IconButton(onClick = { onRatingChanged(i.toFloat()) }) {
//            if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder
////            }
//        }
//    }
//}


