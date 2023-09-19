package com.zafar.umair.rentoutadmin.model.service

interface ConfigurationService {
  suspend fun fetchConfiguration(): Boolean
  val isShowProductEditButtonConfig: Boolean
}
