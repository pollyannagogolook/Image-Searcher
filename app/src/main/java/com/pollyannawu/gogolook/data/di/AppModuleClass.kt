package com.pollyannawu.gogolook.data.di

import com.pollyannawu.gogolook.data.model.RemoteDataSource
import com.pollyannawu.gogolook.data.model.RemoteDataSourceImpl
import com.pollyannawu.gogolook.data.model.Repository
import com.pollyannawu.gogolook.data.model.RepositoryImpl
import com.pollyannawu.gogolook.network.ApiService

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleClass {



    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun provideRepository(repositoryImpl: RepositoryImpl): Repository



}