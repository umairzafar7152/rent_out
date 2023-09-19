package com.zafar.umair.rentout.model.service

interface ConfigurationService {
  suspend fun fetchConfiguration(): Boolean
  val isShowProductEditButtonConfig: Boolean
}
