package com.pollyannawu.gogolook.data.di

import com.pollyannawu.gogolook.data.model.RemoteDataSource
import com.pollyannawu.gogolook.data.model.RemoteDataSourceImpl
import com.pollyannawu.gogolook.data.model.Repository
import com.pollyannawu.gogolook.data.model.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleClass {

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun provideRepository(repositoryImpl: RepositoryImpl): Repository
}