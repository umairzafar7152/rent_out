package com.zafar.umair.rentout.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
