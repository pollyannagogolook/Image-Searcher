package com.pollyannawu.gogolook.data.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): Repository{
    override fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit) {
        remoteDataSource.getDefaultLayoutByRemoteConfig(defaultLayoutCallback)
    }
}