package com.zafar.umair.rentoutadmin.model.service.module

import com.zafar.umair.rentoutadmin.model.service.AccountService
import com.zafar.umair.rentoutadmin.model.service.ConfigurationService
import com.zafar.umair.rentoutadmin.model.service.LogService
import com.zafar.umair.rentoutadmin.model.service.StorageService
import com.zafar.umair.rentoutadmin.model.service.impl.AccountServiceImpl
import com.zafar.umair.rentoutadmin.model.service.impl.ConfigurationServiceImpl
import com.zafar.umair.rentoutadmin.model.service.impl.LogServiceImpl
import com.zafar.umair.rentoutadmin.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

  @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

  @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

  @Binds
  abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService
}
