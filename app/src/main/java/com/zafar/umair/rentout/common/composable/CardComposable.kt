package com.zafar.umair.rentout.common.composable

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun DangerousCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.primary, modifier)
}

@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.onSurface, modifier)
}

@Composable
fun ReviewCardEditor(
    title: String,
    content: String,
    rating: Float,
    dated: String,
    modifier: Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(modifier = Modifier.weight(2f), horizontalAlignment = Alignment.Start) {
                if (content.isNotBlank()) {
                    Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
                }
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
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Dated: $dated", modifier = Modifier.padding(16.dp, 0.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
    Card(

        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(title),
                    color = highlightColor
                )
            }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = highlightColor
            )
        }
    }
}

//@Composable
//fun CardSelector(
//    @StringRes label: Int,
//    options: List<String>,
//    modifier: Modifier,
//    onNewValue: (String) -> Unit
//) {
//    Card(modifier = modifier) {
//        DropdownSelector(label, "", options, Modifier.dropdownSelector(), true, onNewValue)
//    }
//}
