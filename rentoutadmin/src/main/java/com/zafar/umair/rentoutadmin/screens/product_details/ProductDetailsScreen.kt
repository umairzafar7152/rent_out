@file:OptIn(ExperimentalLayoutApi::class)

package com.zafar.umair.rentoutadmin.screens.product_details

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentoutadmin.common.composable.*
import com.zafar.umair.rentoutadmin.common.ext.fieldModifier
import com.zafar.umair.rentoutadmin.common.ext.spacer
import com.zafar.umair.rentoutadmin.common.ext.toolbarActions
import com.zafar.umair.rentoutadmin.R.drawable as AppIcon
import com.zafar.umair.rentoutadmin.R.string as AppText
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.zafar.umair.rentoutadmin.common.ext.basicButton
import com.zafar.umair.rentoutadmin.common.ext.card
import com.zafar.umair.rentoutadmin.model.RentalRequest
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProductDetailsScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val product by viewModel.product
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val selectedStartDate = MutableStateFlow(Calendar.getInstance())
    val selectedEndDate = MutableStateFlow(Calendar.getInstance())
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

//            viewModel::onStartDateChange
            RegularCardEditor(
                AppText.request_start_date,
                AppIcon.ic_clock,
                dateFormat.format(selectedStartDate.collectAsState().value.time),
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
                dateFormat.format(selectedEndDate.collectAsState().value.time),
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
