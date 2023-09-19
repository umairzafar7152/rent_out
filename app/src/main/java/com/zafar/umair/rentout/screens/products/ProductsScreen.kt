package com.zafar.umair.rentout.screens.products

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zafar.umair.rentout.RentOutConstants.ALL_CATEGORY
import com.zafar.umair.rentout.RentOutConstants.ALL_TAB
import com.zafar.umair.rentout.RentOutConstants.FAVORITES_TAB
import com.zafar.umair.rentout.RentOutConstants.NEARBY_TAB
import com.zafar.umair.rentout.RentOutConstants.RENTED_ITEMS_TAB
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.common.composable.ActionToolbar
import com.zafar.umair.rentout.common.composable.BottomSheet
import com.zafar.umair.rentout.common.ext.smallSpacer
import com.zafar.umair.rentout.common.ext.toolbarActions
import com.zafar.umair.rentout.model.FavoriteDocument
import com.zafar.umair.rentout.model.Product


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductsScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel()
) {
//    val context = LocalContext.current
    val products = viewModel.filteredProducts.value.collectAsStateWithLifecycle(emptyList())
    val myRentalRequests = viewModel.myRentalRequests.collectAsStateWithLifecycle(emptyList())
    val favList = viewModel.favList.collectAsStateWithLifecycle(initialValue = null)
    val allProducts = viewModel.products.collectAsStateWithLifecycle(emptyList())
    val ratingReview = viewModel.ratingReviews.collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options
    val favOptions by viewModel.favOptions
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    var showSheet by remember { mutableStateOf(false) }
    var categoryFilterVal: String by remember { mutableStateOf(ALL_CATEGORY) }
    var priceFilterVal: Double by remember { mutableStateOf(0.0) }
    var selectedTab by remember { mutableStateOf(0) }
    val listOfTabs = listOf(
        ALL_TAB,
        FAVORITES_TAB,
        NEARBY_TAB,
        RENTED_ITEMS_TAB
    )

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            if (listOfTabs[selectedTab] == ALL_TAB)
                FloatingActionButton(
                    onClick = {
                        showSheet = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.padding(16.dp)
                ) {
                    Icon(painterResource(id = AppIcon.baseline_filter), "Filter")
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
                    title = AppText.products,
                    modifier = Modifier.toolbarActions(),
                    endActionIcon = AppIcon.ic_settings,
                    endAction = { viewModel.onSettingsClick(openScreen) }
                )

                Spacer(modifier = Modifier.smallSpacer())

                CustomTabs(
                    listOfTabs
                ) {
                    selectedTab = it
                    if(listOfTabs[selectedTab] == RENTED_ITEMS_TAB) {
                        viewModel.updateRentRequestStatus(myRentalRequests.value)
                    }
                }
                when (listOfTabs[selectedTab]) {
                    ALL_TAB -> AllProductsTab(
                        products = products,
                        ratingReview = ratingReview,
                        options = options,
                        openScreen = openScreen
                    )

                    FAVORITES_TAB -> FavoritesTab(
                        products = getFavProducts(allProducts.value, favList.value).toList(),
                        ratingReview = ratingReview,
                        options = favOptions,
                        openScreen = openScreen
                    )

                    NEARBY_TAB -> NearbyTab(
                        products = allProducts,
                        location = viewModel.currentLocation,
                        openScreen = openScreen,
                    )

                    RENTED_ITEMS_TAB -> RequestedTab(
                        myRequests = myRentalRequests,
                        products = products,
                        ratingReview = ratingReview
                    )

                }
            }
        }
    )
    if (showSheet) {
        BottomSheet(
            categoryFilterVal,
            priceFilterVal,
            onCategoryChange = {
                categoryFilterVal = it
            },
            onClearFilters = {
                showSheet = false
                viewModel.clearFilters()
            },
            onPriceChange = {
                priceFilterVal = it.toDouble()
            },
            onApplyFilters = {
                showSheet = false
                viewModel.getFilterList(categoryFilterVal, priceFilterVal)
            }
        ) {
            showSheet = false
        }
    }
    LaunchedEffect(viewModel) { viewModel.loadProductOptions() }
}

@Composable
fun CustomTabs(list: List<String>, callBack: (Int) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }

    ScrollableTabRow(selectedTabIndex = selectedIndex,
        backgroundColor = Color.White,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp),
        edgePadding = 0.dp,
//            .clip(RoundedCornerShape(40)).border(1.dp, color = MaterialTheme.colorScheme.primary),
        indicator = { _: List<TabPosition> ->
            Box {}
        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(60))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(1.dp)
                else Modifier
                    .clip(RoundedCornerShape(60))
                    .background(Color.White)
                    .padding(1.dp),
                selected = selected,
                onClick = {
                    selectedIndex = index
                    callBack(index)
                },
                text = {
                    Text(
                        text = text,
                        color = if (selected) Color.White else MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}

fun getFavProducts(products: List<Product>, favList: List<FavoriteDocument>?): ArrayList<Product?> {
    val favs: ArrayList<Product?> = ArrayList()
    if (!favList.isNullOrEmpty()) {
        for (p in products) {
            if (favList[0].productIds.contains(p.id)) {
                favs.add(p)
            }
        }
    }
    return favs
}

