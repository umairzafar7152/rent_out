package com.zafar.umair.rentoutadmin.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zafar.umair.rentoutadmin.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadImage(path: String) {
    if(path!="") {
        GlideImage(
            model = path,
            contentDescription = "load product image",
            modifier = Modifier.size(120.dp)
        ) {
            it.error(Icons.Default)
                .load(path)
        }
    } else {
        Card(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Image(
                painterResource(R.drawable.baseline_person),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}