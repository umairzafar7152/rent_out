package com.zafar.umair.rentoutadmin.model.service.impl

import com.zafar.umair.rentoutadmin.model.service.ConfigurationService
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.trace
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class ConfigurationServiceImpl @Inject constructor() : ConfigurationService {
  private val remoteConfig
    get() = Firebase.remoteConfig

  init {
    if (BuildConfig.DEBUG) {
      val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
      remoteConfig.setConfigSettingsAsync(configSettings)
    }

  }

  override suspend fun fetchConfiguration(): Boolean =
    trace(FETCH_CONFIG_TRACE) { remoteConfig.fetchAndActivate().await() }

  override val isShowProductEditButtonConfig: Boolean
    get() = remoteConfig[SHOW_PRODUCT_EDIT_BUTTON_KEY].asBoolean()

  companion object {
    private const val SHOW_PRODUCT_EDIT_BUTTON_KEY = "show_product_edit_button"
    private const val FETCH_CONFIG_TRACE = "fetchConfig"
  }
}
