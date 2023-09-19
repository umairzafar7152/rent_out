package com.zafar.umair.rentoutadmin.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zafar.umair.rentoutadmin.common.snackbar.SnackbarManager
import com.zafar.umair.rentoutadmin.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.zafar.umair.rentoutadmin.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class RentOutAdminViewModel(private val logService: LogService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}

