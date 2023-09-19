package com.zafar.umair.rentout.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zafar.umair.rentout.common.snackbar.SnackbarManager
import com.zafar.umair.rentout.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.zafar.umair.rentout.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class RentOutViewModel(private val logService: LogService) : ViewModel() {
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

