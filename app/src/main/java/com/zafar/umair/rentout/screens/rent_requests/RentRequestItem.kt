package com.zafar.umair.rentout.screens.rent_requests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentout.common.composable.*
import com.zafar.umair.rentout.common.ext.fieldModifier
import com.zafar.umair.rentout.common.ext.roundText
import com.zafar.umair.rentout.common.ext.spacer
import com.zafar.umair.rentout.common.ext.toolbarActions

@Composable
fun RentRequestItem(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RentReqItemViewModel = hiltViewModel(),
) {
    val isUploading = remember {
        mutableStateOf(false)
    }
    val product by viewModel.product
    val request by viewModel.request
    val customer by viewModel.customer

//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            ActionToolbar(
                title = AppText.respond_to_rent_request,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_check,
                endAction = {
                    viewModel.onDoneClick(popUpScreen)
                }
            )
            when (request.requestStatus) {
                "Pending" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Respond", fontSize = 16.sp)
                        ResponseButton(
                            text = AppText.approve,
                            Modifier,
                            MaterialTheme.colorScheme.primary
                        ) {
                            isUploading.value = true
                            viewModel.onApproveClick().runCatching {
                                isUploading.value = false
                                popUpScreen()
                            }
                        }
                        ResponseButton(text = AppText.reject, Modifier, Color.Gray) {
                            isUploading.value = true
                            viewModel.onRejectClick().runCatching {
                                isUploading.value = false
                                popUpScreen()
                            }
                        }
                    }
                }
                "Approved" -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "You approved this Request",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                "Rejected" -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "You rejected this Request",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.roundText(Color.Gray)
                        )
                    }
                }
            }
            Divider()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Customer Details", fontSize = 16.sp)
            }
            BasicField(
                AppText.customer_email,
                customer.email,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                {},
                Modifier.fieldModifier()
            )
            Divider()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Request Details", fontSize = 16.sp)
            }
            BasicField(
                AppText.request_start_date,
                request.rentalStartDate,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                {},
                Modifier.fieldModifier()
            )
            BasicField(
                AppText.request_end_date,
                request.rentalEndDate,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                {},
                Modifier.fieldModifier()
            )
            BasicField(
                AppText.request_time,
                request.requestTime,
                1,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                false,
                {},
                Modifier.fieldModifier()
            )
            Divider()
            Spacer(modifier = Modifier.spacer())
            DropdownSelector(
                options = listOf("DIY Tools", "Costumes", "Tech Gadgets", "Others"),
                value = product.category,
                modifier = Modifier.fieldModifier(),
                onNewValue = {},
                isEnabled = false,
                label = AppText.category
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageCard(
                    if (product.photos[0] != "") {
                        product.photos[0]
                    } else if (product.photos[1] != "") {
                        product.photos[1]
                    } else if (product.photos[2] != "") {
                        product.photos[2]
                    } else if (product.photos[3] != "") {
                        product.photos[3]
                    } else if (product.photos[4] != "") {
                        product.photos[4]
                    } else if (product.photos[5] != "") {
                        product.photos[5]
                    } else {
                        ""
                    }
                )
                Column {
                    BasicField(
                        AppText.title,
                        product.title,
                        1,
                        KeyboardOptions(keyboardType = KeyboardType.Text),
                        false,
                        {},
                        Modifier.fieldModifier()
                    )
                    BasicField(
                        AppText.cost,
                        product.cost.toString(),
                        1,
                        KeyboardOptions(keyboardType = KeyboardType.Number),
                        false,
                        {},
                        Modifier.fieldModifier()
                    )
                }
            }
            Spacer(modifier = Modifier.spacer())
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
) {
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier
            .padding(all = 8.dp)
            .size(110.dp)
            .border(1.dp, Color.Black)
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