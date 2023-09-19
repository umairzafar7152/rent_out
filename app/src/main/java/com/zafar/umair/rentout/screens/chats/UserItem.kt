package com.zafar.umair.rentout.screens.chats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zafar.umair.rentout.common.composable.DropdownContextMenu
import com.zafar.umair.rentout.common.composable.LoadImage
import com.zafar.umair.rentout.common.ext.contextMenu
import com.zafar.umair.rentout.model.User

@Composable
fun UserItem(
    user: User,
    options: List<String>,
    onActionClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(),
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            LoadImage(path = "")
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            ) {
                Text(text = user.name, style = MaterialTheme.typography.titleSmall)
//                Text(text = user.fcmToken, style = MaterialTheme.typography.bodySmall)
            }

//            if (product.flag) {
//                Icon(
//                    painter = painterResource(AppIcon.ic_flag),
//                    tint = DarkOrange,
//                    contentDescription = "Flag"
//                )
//            }
            DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
        }
    }
}



