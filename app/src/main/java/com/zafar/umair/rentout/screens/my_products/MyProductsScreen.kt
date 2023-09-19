package com.zafar.umair.rentout.screens.my_products

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.common.composable.ActionToolbar
import com.zafar.umair.rentout.common.composable.EmptyListText
import com.zafar.umair.rentout.common.ext.smallSpacer
import com.zafar.umair.rentout.common.ext.toolbarActions
import com.zafar.umair.rentout.model.service.module.FirebaseModule.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyProductsScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyProductsViewModel = hiltViewModel()
) {
    val myProducts = viewModel.myProducts.collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!auth().currentUser!!.isAnonymous)
                        viewModel.onAddClick(openScreen)
                    else {
                        coroutineScope.launch {
//                            val snackbarResult =
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Log in to rent a product",
                                actionLabel = null
                            )
//                            when (snackbarResult) {
//                                SnackbarResult.Dismissed -> TODO()
//                                SnackbarResult.ActionPerformed -> TODO()
//                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActionToolbar(
                    title = AppText.my_products,
                    modifier = Modifier.toolbarActions(),
                    endActionIcon = AppIcon.ic_settings,
                    endAction = { viewModel.onSettingsClick(openScreen) }
                )

                Spacer(modifier = Modifier.smallSpacer())
                if (myProducts.value.isEmpty()) {
                    EmptyListText()
                } else {
                    LazyColumn {
                        items(myProducts.value.size, key = { it }) { productItem ->
                            MyProductItem(
                                product = myProducts.value[productItem],
                                options = options,
//              onCheckChange = { viewModel.onTaskCheckChange(tasks.value[productItem]) },
                                onActionClick = { action ->
                                    viewModel.onProductActionClick(
                                        openScreen,
                                        myProducts.value[productItem],
                                        action
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    LaunchedEffect(viewModel) { viewModel.loadProductOptions() }
}

