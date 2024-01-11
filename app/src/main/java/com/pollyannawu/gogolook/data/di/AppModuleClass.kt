package com.pollyannawu.gogolook.data.di


import com.pollyannawu.gogolook.data.model.Repository
import com.pollyannawu.gogolook.data.model.RepositoryImpl
import com.pollyannawu.gogolook.data.model.remote_config.RemoteConfigDataSource
import com.pollyannawu.gogolook.data.model.remote_config.RemoteConfig

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleClass {




    @Binds
    abstract fun provideRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    abstract fun provideRemoteConfig(remoteConfigDataSource: RemoteConfigDataSource): RemoteConfig



}