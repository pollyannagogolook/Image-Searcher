package com.pollyannawu.gogolook.data.model

import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): Repository{
    override fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit) {
        remoteDataSource.getDefaultLayoutByRemoteConfig(defaultLayoutCallback)
    }

    override suspend fun getImagesFromPixabayAPI(input: String): Result<List<Hit>> {
        return remoteDataSource.getImagesFromPixabayAPI(input)
    }
}