package com.zafar.umair.rentoutadmin.model.service.impl

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.zafar.umair.rentoutadmin.model.service.LogService
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
  override fun logNonFatalCrash(throwable: Throwable) =
    FirebaseCrashlytics.getInstance().recordException(throwable)
}
