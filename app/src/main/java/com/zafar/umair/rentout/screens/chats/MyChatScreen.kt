package com.zafar.umair.rentout.screens.chats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.common.composable.ChatActionToolbar
import com.zafar.umair.rentout.common.composable.ChatTextField
import com.zafar.umair.rentout.common.ext.smallSpacer
import com.zafar.umair.rentout.common.ext.toolbarActions


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyChatScreen(
    popUpScreen: (String) -> Unit,
    viewModel: MyChatViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle(emptyList())
//    val newMessage by viewModel.newMessage
    val chatMessages = viewModel.chatMessages.collectAsStateWithLifecycle(emptyList())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChatActionToolbar(
                    title = "Chat with ${users.find { it.id == viewModel.recipientId }?.name}",
                    modifier = Modifier.toolbarActions(),
                    endActionIcon = AppIcon.ic_settings,
                    endAction = { viewModel.onSettingsClick(popUpScreen) }
                )

                Spacer(modifier = Modifier.smallSpacer())
                if (chatMessages.value.isEmpty()) {
                    EmptyListText()
                } else {
                    LazyColumn(modifier = Modifier.weight(1F), state = LazyListState(chatMessages.value.size-1)) {
                        items(chatMessages.value.size, key = { it }) { chat ->
                            ChatItem(
                                viewModel.isSelf(chatMessages.value[chat]),
                                message = chatMessages.value[chat],
                            )
                        }
                    }
                }

                ChatTextField(
                    text = AppText.chat_text_field_text,
                    value = viewModel.newMessage.value.text,
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isVisible = viewModel.newMessage.value.text.isNotEmpty(),
                    onNewValue = viewModel::onMessageChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    viewModel.onSendClick()
                }
            }
        }
    )
}

@Composable
private fun EmptyListText() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No chat found!",
            textAlign = TextAlign.Center
        )
    }
}
