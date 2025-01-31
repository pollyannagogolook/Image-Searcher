package com.pollyannawu.gogolook.data.model

import android.database.Cursor
import androidx.paging.PagingData
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.model.image_search.ImageRepository
import com.pollyannawu.gogolook.data.model.remote_config.RemoteConfigRepository
import com.pollyannawu.gogolook.data.model.search_history.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val imageRepository: ImageRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val searchHistoryRepository: SearchHistoryRepository): Repository{



    override suspend fun getImageBySearch(query: String): Flow<PagingData<Hit>> {
        return imageRepository.getImageBySearch(query)
    }

    override fun getDefaultLayoutByRemoteConfig(key: String, fallback: String) : String{
        return remoteConfigRepository.getDefaultLayout(key, fallback)
    }

    override fun getSearchHistorySuggestion(query: String): Cursor?{
        return searchHistoryRepository.getSearchHistorySuggestion(query)
    }

    override fun saveSearchQuery(query: String) {
        searchHistoryRepository.insertSearchQuery(query)
    }
}