package com.zafar.umair.rentout.screens.chats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zafar.umair.rentout.model.TextMessage

@Composable
fun ChatItem(
    isSelf: Boolean,
    message: TextMessage
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = if (isSelf) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = if (!isSelf) MaterialTheme.colorScheme.surfaceVariant else Color.White
            ),
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
                .wrapContentSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = message.text, style = MaterialTheme.typography.titleSmall)
//                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message.senderId, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message.time.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }

}



