package com.pollyannawu.gogolook.data.model

import android.database.Cursor
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): Repository{
    override suspend fun getAllImages(): Result<List<Hit>> {
        return remoteDataSource.getAllImages()
    }

    override fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit) {
        remoteDataSource.getDefaultLayoutByRemoteConfig(defaultLayoutCallback)
    }

    override suspend fun getImagesFromPixabayAPI(input: String): Result<List<Hit>> {
        return remoteDataSource.getImagesFromPixabayAPI(input)
    }

    override fun updateSearchHistorySuggestion(query: String): Cursor? {
       return remoteDataSource.updateSearchHistorySuggestion(query)
    }

    override fun saveSearchQuery(query: String) {
        remoteDataSource.saveSearchQuery(query)
    }
}