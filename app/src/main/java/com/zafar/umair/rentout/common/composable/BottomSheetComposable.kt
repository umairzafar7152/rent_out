package com.zafar.umair.rentout.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zafar.umair.rentout.R
import com.zafar.umair.rentout.RentOutConstants
import com.zafar.umair.rentout.RentOutConstants.ALL_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.COSTUMES_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.DIY_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.ELECTRONICS_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.EVENT_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.FASHION_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.OTHERS_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.OUTDOORS_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.TECH_CATEGORY
import com.zafar.umair.rentout.common.ext.fieldModifier
import com.zafar.umair.rentout.common.ext.textButtonWrapText
import com.zafar.umair.rentout.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    categoryVal: String,
    priceVal: Double,
    onCategoryChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier.heightIn(min = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Apply Filters", fontWeight = FontWeight.Bold)
                BasicOutlinedButton(
                    text = AppText.clear_filters,
                    modifier = Modifier.textButtonWrapText()
                ) {
                    onClearFilters()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CategoriesFilter(categoryVal, valueChange = onCategoryChange)
            Spacer(modifier = Modifier.height(16.dp))
            BasicField(
                text = AppText.max_price,
                value = priceVal.toString(),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isEnabled = true,
                onNewValue = onPriceChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicButton(
                text = AppText.apply_filters,
                modifier = Modifier
                    .textButtonWrapText()
                    .align(Alignment.CenterHorizontally),
            ) {
                onApplyFilters()
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
private fun CategoriesFilter(value: String, valueChange: (String) -> Unit) {
    DropdownSelector(
        options = listOf(
            ALL_CATEGORY,
            ELECTRONICS_CATEGORY,
            OUTDOORS_CATEGORY,
            FASHION_CATEGORY,
            EVENT_CATEGORY,
            DIY_CATEGORY,
            COSTUMES_CATEGORY,
            TECH_CATEGORY,
            OTHERS_CATEGORY
        ),
        value = value,
        modifier = Modifier.fieldModifier(),
        onNewValue = valueChange,
        isEnabled = true,
        label = R.string.category
    )
}