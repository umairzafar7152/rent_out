package com.zafar.umair.rentout.screens.chats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.common.composable.ActionToolbar
import com.zafar.umair.rentout.common.ext.smallSpacer
import com.zafar.umair.rentout.common.ext.toolbarActions


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AllChatsScreen(
    openScreen: (String) -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val users = viewModel.chatUsers.collectAsStateWithLifecycle(emptyList())
//    val options by viewModel.options
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActionToolbar(
                    title = AppText.chats,
                    modifier = Modifier.toolbarActions(),
                    endActionIcon = AppIcon.ic_settings,
                    endAction = { viewModel.onSettingsClick(openScreen) }
                )

                Spacer(modifier = Modifier.smallSpacer())

                if (users.value.isEmpty()) {
                    EmptyListText()
                } else {
                    LazyColumn {
                        items(users.value.size, key = { it }) { userItem ->
                            if (users.value[userItem].id != "RNxUFFjLPdhRgHd7QSEIKHeEoW33")
                                UserItem(
                                    user = users.value[userItem],
                                    options = listOf("Open Chat"),
//              onCheckChange = { viewModel.onTaskCheckChange(tasks.value[userItem]) },
                                    onActionClick = {
                                        viewModel.onUserClick(users.value[userItem].id, openScreen)
//                                    viewModel.onProductActionClick(
//                                        openScreen,
//                                        products.value[userItem],
//                                        action
//                                    )
                                    }
                                )
                        }
                    }
                }
            }
        }
    )

//    LaunchedEffect(viewModel) { viewModel.loadProductOptions() }
}


@Composable
private fun EmptyListText() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No users found!",
            textAlign = TextAlign.Center
        )
    }
}
