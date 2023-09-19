@file:OptIn(ExperimentalLayoutApi::class)

package com.zafar.umair.rentout.screens.edit_product

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentout.common.composable.*
import com.zafar.umair.rentout.common.ext.fieldModifier
import com.zafar.umair.rentout.common.ext.spacer
import com.zafar.umair.rentout.common.ext.toolbarActions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import androidx.compose.runtime.getValue
import com.zafar.umair.rentout.RentOutConstants.COSTUMES_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.DIY_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.ELECTRONICS_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.EVENT_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.FASHION_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.OTHERS_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.OUTDOORS_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.TECH_CATEGORY
import com.zafar.umair.rentout.model.CostBy

@Composable
fun EditProductScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProductViewModel = hiltViewModel(),
) {
    val isUploading = remember {
        mutableStateOf(false)
    }
    val product by viewModel.product
//    val uriList by viewModel.uriList
    val coroutineScope = rememberCoroutineScope()
    val costBy = listOf(CostBy.DAY.costDuration, CostBy.HOUR.costDuration)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(costBy[0]) }
    val context = LocalContext.current
    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionToolbar(
                title = AppText.edit_product,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_check,
                endAction = {
                    if (product.postingDateTime == "") {
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK)
                        val currentDateAndTime = sdf.format(Date())
                        viewModel.onDateTimeChange(currentDateAndTime)
                    }
                    isUploading.value = true
                    viewModel.uploadImagesToFirebase(coroutineScope) {
                        isUploading.value = false
                        viewModel.onDoneClick(context, popUpScreen)
                    }
                }
            )

            DropdownSelector(
                options = listOf(
                    DIY_CATEGORY,
                    ELECTRONICS_CATEGORY,
                    OUTDOORS_CATEGORY,
                    FASHION_CATEGORY,
                    EVENT_CATEGORY,
                    COSTUMES_CATEGORY,
                    TECH_CATEGORY,
                    OTHERS_CATEGORY
                ),
                value = product.category,
                modifier = Modifier.fieldModifier(),
                onNewValue = viewModel::onCategoryChange,
                isEnabled = true,
                label = AppText.category
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            ) {
                ImageCard(
                    product.photos[0],
                    viewModel::getUriAt,
                    0,
                    viewModel::onImageChange
                )
                ImageCard(
                    product.photos[1],
                    viewModel::getUriAt,
                    1,
                    viewModel::onImageChange
                )
                ImageCard(
                    product.photos[2],
                    viewModel::getUriAt,
                    2,
                    viewModel::onImageChange
                )
                ImageCard(
                    product.photos[3],
                    viewModel::getUriAt,
                    3,
                    viewModel::onImageChange
                )
                ImageCard(
                    product.photos[4],
                    viewModel::getUriAt,
                    4,
                    viewModel::onImageChange
                )
                ImageCard(
                    product.photos[5],
                    viewModel::getUriAt,
                    5,
                    viewModel::onImageChange
                )
            }

            Spacer(modifier = Modifier.spacer())

            val fieldModifier = Modifier.fieldModifier()
            BasicField(
                AppText.title,
                product.title,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                true,
                viewModel::onTitleChange,
                fieldModifier
            )
            BasicField(
                AppText.description,
                product.description,
                4,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                true,
                viewModel::onDescriptionChange,
                fieldModifier
            )
            BasicField(
                AppText.cost,
                product.cost.toString(),
                1,
                KeyboardOptions(keyboardType = KeyboardType.Number),
                true,
                viewModel::onCostChange,
                fieldModifier
            )

//            costBy.forEach { text ->
//                Row(Modifier
//                    .fillMaxWidth()
//                    .selectable(
//                        selected = (text == selectedOption),
//                        onClick = { onOptionSelected(text) }
//                    )
//                    .padding(horizontal = 16.dp)
//                ) {
//                    RadioButton(
//                        // inside this method we are
//                        // adding selected with a option.
//                        selected = (text == selectedOption),
//                        modifier = Modifier.padding(all = Dp(value = 8F)),
//                        onClick = {
//                            // inside on click method we are setting a
//                            // selected option of our radio buttons.
//                            onOptionSelected(text)
//
//                            // after clicking a radio button
//                            // we are displaying a toast message.
//                        }
//                    )
//                }
//            }


//        BasicField(
//            AppText.category,
//            product.category,
//            viewModel::onCategoryChange,
//            fieldModifier
//        )
//    BasicField(AppText.url, product., viewModel::onUrlChange, fieldModifier)
//        Spacer(modifier = Modifier.spacer())
//        CardEditors(product, viewModel::onDateChange, viewModel::onTimeChange)
//    CardSelectors(product, viewModel::onPriorityChange, viewModel::onFlagToggle)

        }
        if (isUploading.value) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {}
            )
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ImageCard(
    uriParamFirebase: String,
    uriParamLocal: (Int) -> String,
    index: Int,
    onImageChange: (Int, String) -> Unit
) {
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageChange(index, uri.toString())
    }
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier
            .padding(all = 8.dp)
            .size(110.dp)
            .clickable {
                launcher.launch("image/*")
            },
    ) {
        val t1 = uriParamLocal(index)
        if (t1 == "") {
            if (uriParamFirebase == "") {
                Column(
                    modifier = Modifier
                        .padding(all = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Image",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                LoadImage(path = uriParamFirebase)
            }
        } else {
            Uri.parse(t1).let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)

                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp)
                    )
                }
            }
        }
    }
}


//@Composable
//private fun CardEditors(
//    product: Product,
//    onDateChange: (Long) -> Unit,
//    onTimeChange: (Int, Int) -> Unit
//) {
//    val activity = LocalContext.current as AppCompatActivity
//
//    RegularCardEditor(AppText.date, AppIcon.ic_calendar, product.postingDate, Modifier.card()) {
//        showDatePicker(activity, onDateChange)
//    }
//
//    RegularCardEditor(AppText.time, AppIcon.ic_clock, product.postingTime, Modifier.card()) {
//        showTimePicker(activity, onTimeChange)
//    }
//}

//@Composable
//private fun CardSelectors(
//  product: Product,
//  onPriorityChange: (String) -> Unit,
//  onFlagToggle: (String) -> Unit
//) {
//  CardSelector(AppText.priority, Priority.getOptions(), prioritySelection, Modifier.card()) {
//    newValue ->
//    onPriorityChange(newValue)
//  }
//
//  val flagSelection = EditFlagOption.getByCheckedState(product.flag).name
//  CardSelector(AppText.flag, EditFlagOption.getOptions(), flagSelection, Modifier.card()) { newValue
//    ->
//    onFlagToggle(newValue)
//  }
//}

//private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
//    val picker = MaterialDatePicker.Builder.datePicker().build()
//
//    activity?.let {
//        picker.show(it.supportFragmentManager, picker.toString())
//        picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
//    }
//}
//
//private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
//    val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
//
//    activity?.let {
//        picker.show(it.supportFragmentManager, picker.toString())
//        picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
//    }
//}
