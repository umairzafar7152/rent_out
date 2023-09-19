package com.zafar.umair.rentout.screens.rent_requests

import android.util.Log
import com.zafar.umair.rentout.model.RentalRequest
import com.zafar.umair.rentout.model.RequestStatusOption
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RentRequestsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService
) : RentOutViewModel(logService) {
    val myProducts = storageService.myProducts
    val requests = storageService.othersRentalRequests
    val ratingReviews = storageService.ratingReviews

    fun updateRentRequestStatus(requestList: List<RentalRequest>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = System.currentTimeMillis()
//        myRentalRequests.mapLatest {
//        myRentalRequests.mapLatest {
        requestList.forEach { request ->
            Log.d("STATUS", "Before updating")
            val startDate = dateFormat.parse(request.rentalStartDate)
            val endDate = dateFormat.parse(request.rentalEndDate)
            launchCatching {
                if (currentDate >= startDate!!.time && currentDate <= endDate!!.time && request.requestStatus == RequestStatusOption.Approved.title) {
                    Log.d("STATUS", "updated to ongoing")
                    storageService.updateRequestStatus(
                        RequestStatusOption.Ongoing.title,
                        request.id
                    )
                } else if (currentDate >= startDate.time && currentDate <= endDate!!.time && request.requestStatus == RequestStatusOption.Pending.title) {
                    Log.d("STATUS", "Updated to expired")
                    storageService.updateRequestStatus(
                        RequestStatusOption.Expired.title,
                        request.id
                    )
                } else if (currentDate > endDate!!.time && request.requestStatus == RequestStatusOption.Pending.title) {
                    Log.d("STATUS", "Updated to expired")
                    storageService.updateRequestStatus(
                        RequestStatusOption.Expired.title,
                        request.id
                    )
                } else if (currentDate > endDate.time && request.requestStatus == RequestStatusOption.Approved.title) {
                    Log.d("STATUS", "Updated to rented")
                    storageService.updateRequestStatus(RequestStatusOption.Rented.title, request.id)
                }
            }
        }
//        }
    }
}
