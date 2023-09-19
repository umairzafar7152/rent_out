package com.zafar.umair.rentoutadmin.screens.rent_requests

import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.screens.RentOutAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RentRequestsViewModel @Inject constructor(
    logService: LogService,
    storageService: StorageService
) : RentOutAdminViewModel(logService) {
    val myProducts = storageService.myProducts
    val requests = storageService.myRentalRequests
    val ratingReviews = storageService.ratingReviews
}
