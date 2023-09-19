package com.zafar.umair.rentoutadmin.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
