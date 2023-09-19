package com.zafar.umair.rentoutadmin.screens.products

import android.content.Context
import com.zafar.umair.rentoutadmin.R.drawable as AppIcon
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.zafar.umair.rentoutadmin.R.string as AppText
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
import com.zafar.umair.rentoutadmin.common.composable.EmptyListText
import com.zafar.umair.rentoutadmin.common.composable.ResponseButton
import com.zafar.umair.rentoutadmin.common.ext.filledBasicButton
import com.zafar.umair.rentoutadmin.model.Product

@Composable
fun NearbyTab(
    products: State<List<Product>>,
    location: State<LatLng>,
    openScreen: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
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
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
//                    loading.value = false
                }
            ) {
//                Circle(
//                    center = location.value,
//                    radius = 2000 / 111000f.toDouble(),
//                    strokeWidth = 1.0F
//                )
                products.value.forEach { product ->
                    if (product.lat != 0.0 && product.lon != 0.0) {
                        Log.d("LOCATION", "INSIDE NEARBY: ${product.lat}, ${product.lon}")
                        val icon = bitmapDescriptor(context, AppIcon.baseline_filter)
                        Marker(
                            state = MarkerState(
                                position = LatLng(product.lat, product.lon)
                            ),
                            icon = icon,
                            title = product.title,
                            snippet = "${product.cost} ${product.costBy}",
                            onInfoWindowClick = {
                                Log.d("LOCATION", "CLICKED: ${product.lat}, ${product.lon}")
                                viewModel.onMapMarkerClick(product.id, openScreen)
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
