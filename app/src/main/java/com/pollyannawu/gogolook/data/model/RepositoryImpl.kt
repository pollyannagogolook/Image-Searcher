package com.pollyannawu.gogolook.data.model

import com.pollyannawu.gogolook.data.dataclass.Hit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): Repository{
    override fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit) {
        remoteDataSource.getDefaultLayoutByRemoteConfig(defaultLayoutCallback)
    }

    override fun getImagesFromPixabayAPI(hitCallbacks: (List<Hit>) -> Unit) {
        remoteDataSource.getImagesFromPixabayAPI(hitCallbacks)
    }
}