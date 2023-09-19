package com.zafar.umair.rentout.screens.products

import android.content.Context
import com.zafar.umair.rentout.R.drawable as AppIcon
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.zafar.umair.rentout.R.string as AppText
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.zafar.umair.rentout.common.composable.DialogCancelButton
import com.zafar.umair.rentout.common.composable.DialogConfirmButton
import com.zafar.umair.rentout.common.composable.EmptyListText
import com.zafar.umair.rentout.common.composable.ResponseButton
import com.zafar.umair.rentout.common.ext.filledBasicButton
import com.zafar.umair.rentout.model.Product

@Composable
fun NearbyTab(
    products: State<List<Product>>,
    location: State<LatLng>,
    openScreen: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    var productId by remember {
        mutableStateOf("")
    }
    var showProductDetailDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isButtonVisible = remember {
        mutableStateOf(true)
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location.value, 13f)

    }
    if (products.value.isEmpty()) {
        EmptyListText()
    } else {
        if (isButtonVisible.value) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ResponseButton(
                        text = AppText.find_nearby,
                        modifier = Modifier.filledBasicButton(MaterialTheme.colorScheme.primary),
                        MaterialTheme.colorScheme.primary
                    ) {
                        isLoading.value = true
                        viewModel.getLocation(context) {
                            Log.d("LOCATION", "location is: $it")
                            if (it) {
                                isLoading.value = false
                                isButtonVisible.value = false
                            }
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
        } else {
//            LaunchedEffect(cameraPositionState.isMoving) {
            LaunchedEffect(key1 = null) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(location.value, 13f, 0f, 0f)
                    ),
                    durationMs = 1000
                )
            }
            if (showProductDetailDialog) {
                AlertDialog(
                    title = { Text(stringResource(AppText.product_details_title)) },
                    text = { Text(stringResource(AppText.product_details_description)) },
                    dismissButton = { DialogCancelButton(AppText.cancel) { showProductDetailDialog = false } },
                    confirmButton = {
                        DialogConfirmButton(AppText.open) {
                            viewModel.onMapMarkerClick(productId, openScreen)
                            showProductDetailDialog = false
                        }
                    },
                    onDismissRequest = { showProductDetailDialog = false }
                )
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
//                    loading.value = false
                },
            ) {
//                Circle(
//                    center = location.value,
//                    radius = 2000 / 111000f.toDouble(),
//                    strokeWidth = 1.0F
//                )
                products.value.forEach { product ->
                    if (product.lat != 0.0 && product.lon != 0.0) {
                        Log.d("LOCATION", "INSIDE NEARBY: ${product.lat}, ${product.lon}")
                        val icon = bitmapDescriptor(context, AppIcon.baseline_shopping_bag)
                        val randomOffset = LatLng(
                            product.lat + getRandomOffset(),
                            product.lon + getRandomOffset()
                        )
                        Marker(
                            state = MarkerState(
                                position = randomOffset
//                                position = LatLng(product.lat, product.lon)
                            ),
                            icon = icon,
                            title = product.title,
                            snippet = "${product.cost} ${product.costBy}",
                            onInfoWindowClick = {
                                productId = product.id
                                showProductDetailDialog = true
//                                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                                Log.d("LOCATION", "CLICKED: ${product.lat}, ${product.lon}")
//                                viewModel.onMapMarkerClick(product.id, openScreen)
                            }
                        )
                    }
                }
                Marker(
                    state = MarkerState(
                        position = location.value
                    ),
                    title = "My Location",
                    snippet = ""
                )
            }
        }
    }
}

private fun getRandomOffset(): Double {
    // Adjust this value to control the offset range
    val offsetRange = 0.0009
    return (Math.random() * 2 - 1) * offsetRange
}

fun bitmapDescriptor(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
