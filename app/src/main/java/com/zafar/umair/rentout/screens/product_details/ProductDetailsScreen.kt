@file:OptIn(ExperimentalLayoutApi::class)

package com.zafar.umair.rentout.screens.product_details

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentout.common.composable.*
import com.zafar.umair.rentout.common.ext.fieldModifier
import com.zafar.umair.rentout.common.ext.spacer
import com.zafar.umair.rentout.common.ext.toolbarActions
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.zafar.umair.rentout.common.ext.basicButton
import com.zafar.umair.rentout.common.ext.card
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.screens.products.getProductRating
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProductDetailsScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    val product by viewModel.product
    val ratingReviews by viewModel.ratingReviews.collectAsState(initial = emptyList())
    val rating = getProductRating(
        product.id,
        viewModel.ratingReviews.collectAsState(initial = emptyList()).value
    )
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val selectedStartDate = remember {
        mutableStateOf(Calendar.getInstance())
    }
    val selectedEndDate = remember {
        mutableStateOf(Calendar.getInstance())
    }
//    var selectedStartDateText by remember { mutableStateOf("") }
//    var selectedEndDateText by remember { mutableStateOf("") }

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

//    var selectedStartTimeText by remember { mutableStateOf("") }
//    var selectedEndTimeText by remember { mutableStateOf("") }

    // Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val startDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val newDate = Calendar.getInstance()
            newDate.apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
                set(Calendar.HOUR_OF_DAY, selectedStartDate.value.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, selectedStartDate.value.get(Calendar.MINUTE))
            }
            selectedStartDate.value = newDate
//            selectedStartDate.set(Calendar.YEAR, selectedYear)
//            selectedStartDate.set(Calendar.MONTH, selectedMonth)
//            selectedStartDate.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
            Log.d("CALENDAR", "1-${"$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"}")
            Log.d("CALENDAR", "2-${dateFormat.format(selectedStartDate.value.time)}")
//            selectedStartDateText = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
//            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )
    startDatePicker.datePicker.minDate = calendar.timeInMillis

    val startTimePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            val newDate = Calendar.getInstance()
            newDate.apply {
                set(Calendar.YEAR, selectedStartDate.value.get(Calendar.YEAR))
                set(Calendar.MONTH, selectedStartDate.value.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, selectedStartDate.value.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            selectedStartDate.value = newDate
//            selectedStartDate.value.set(Calendar.HOUR_OF_DAY, selectedHour)
//            selectedStartDate.value.set(Calendar.MINUTE, selectedMinute)
//            selectedStartTimeText = "$selectedHour:$selectedMinute"
        }, hour, minute, false
    )

    val endDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
//            selectedEndDate.value.set(Calendar.YEAR, selectedYear)
//            selectedEndDate.value.set(Calendar.MONTH, selectedMonth)
//            selectedEndDate.value.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
            val newDate = Calendar.getInstance()
            newDate.apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
                set(Calendar.HOUR_OF_DAY, selectedEndDate.value.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, selectedEndDate.value.get(Calendar.MINUTE))
            }
            selectedEndDate.value = newDate
//            selectedEndDateText = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
//            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )
    startDatePicker.datePicker.minDate = calendar.timeInMillis

    val endTimePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            val newDate = Calendar.getInstance()
            newDate.apply {
                set(Calendar.YEAR, selectedEndDate.value.get(Calendar.YEAR))
                set(Calendar.MONTH, selectedEndDate.value.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, selectedEndDate.value.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            selectedEndDate.value = newDate
//            selectedEndDate.value.set(Calendar.HOUR_OF_DAY, selectedHour)
//            selectedEndDate.value.set(Calendar.MINUTE, selectedMinute)
//            selectedEndTimeText = "$selectedHour:$selectedMinute"
        }, hour, minute, false
    )

//    var isStartDatePickerVisible by remember { mutableStateOf(false) }
//    var isEndDatePickerVisible by remember { mutableStateOf(false) }
//    var isStartTimePickerVisible by remember { mutableStateOf(false) }
//    var isEndTimePickerVisible by remember { mutableStateOf(false) }
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionToolbar(
                title = AppText.product_details,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_check,
                endAction = {
                    viewModel.onDoneClick(popUpScreen)
                }
            )

            DropdownSelector(
                options = listOf("DIY Tools", "Costumes", "Tech Gadgets", "Others"),
                value = product.category,
                modifier = Modifier.fieldModifier(),
                onNewValue = viewModel::onCategoryChange,
                isEnabled = false,
                label = AppText.category
            )

            Spacer(modifier = Modifier.spacer())

            Text(text = "Choose Rental Dates", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.spacer())
//            viewModel::onStartDateChange
            RegularCardEditor(
                AppText.request_start_date,
                AppIcon.ic_clock,
                dateFormat.format(selectedStartDate.value.time),
//                "$selectedStartDateText $selectedStartTimeText",
                Modifier.card()
            ) {
                startDatePicker.show().runCatching {
                    startTimePicker.show()
                }
            }

            RegularCardEditor(
                AppText.request_end_date,
                AppIcon.ic_clock,
                dateFormat.format(selectedEndDate.value.time),
                Modifier.card()
            ) {
                endDatePicker.show().runCatching {
                    endTimePicker.show()
                }
            }

            BasicButton(text = AppText.send_request, modifier = Modifier.basicButton()) {
                viewModel.onSendRequestClick(
                    context,
                    RentalRequest(
                        productId = product.id,
                        ownerId = product.userId,
                        rentalStartDate = dateFormat.format(selectedStartDate.value.time),
                        rentalEndDate = dateFormat.format(selectedEndDate.value.time)
                    )
                )
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            ) {
                ImageCard(
                    product.photos[0]
                )
                ImageCard(
                    product.photos[1]
                )
                ImageCard(
                    product.photos[2]
                )
                ImageCard(
                    product.photos[3]
                )
                ImageCard(
                    product.photos[4]
                )
                ImageCard(
                    product.photos[5]
                )
            }

            Spacer(modifier = Modifier.spacer())

            val fieldModifier = Modifier.fieldModifier()
            BasicField(
                AppText.title,
                product.title,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                viewModel::onTitleChange,
                fieldModifier
            )
            BasicField(
                AppText.description,
                product.description,
                4,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                viewModel::onDescriptionChange,
                fieldModifier
            )
            BasicField(
                AppText.cost,
                product.cost.toString(),
                1,
                KeyboardOptions(keyboardType = KeyboardType.Number),
                false,
                viewModel::onCostChange,
                fieldModifier
            )
            BasicField(
                AppText.posted_since,
                product.postingDateTime,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                viewModel::onDateTimeChange,
                fieldModifier
            )
            Divider()

            Text("Rating:", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$rating")
                Spacer(modifier = Modifier.width(12.dp))
                RatingBar(
                    value = rating,
                    config = RatingBarConfig()
                        .style(RatingBarStyle.HighLighted),
                    onValueChange = {
                    },
                    onRatingChanged = {
                        Log.d("TAG", "onRatingChanged: $it")
                    }
                )
            }
            Text("Reviews:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            ) {
                items(ratingReviews.size, key = { it }) { productItem ->
                    if (ratingReviews[productItem].productId == product.id) {
                        ReviewCardEditor(
                            title = users.find { it.id == ratingReviews[productItem].ratedUserId }?.name
                                ?: ratingReviews[productItem].ratedUserId,
//                            title = ratingReviews[productItem].ratedUserId,
                            content = ratingReviews[productItem].review,
                            rating = ratingReviews[productItem].rating.toFloat(),
                            dated = ratingReviews[productItem].time,
                            modifier = Modifier.card()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
private fun ImageCard(
    uriParamFirebase: String,
) {
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier
            .padding(all = 8.dp)
            .size(110.dp)
    ) {
        if (uriParamFirebase == "") {
            Column(
                modifier = Modifier
                    .padding(all = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.BrokenImage,
                    contentDescription = "Add Image",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        } else {
            LoadImage(path = uriParamFirebase)
        }
    }
}
