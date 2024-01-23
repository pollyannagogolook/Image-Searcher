package com.pollyannawu.gogolook.data.model.search_history


import android.database.Cursor
import javax.inject.Inject
import javax.inject.Singleton


class SearchHistoryRepository @Inject constructor(private val searchHistoryDataSource: SearchHistoryDataSource){
    fun saveSearchQuery(query: String){
        searchHistoryDataSource.saveSearchQuery(query)

    }
    fun updateSearchHistorySuggestion(query: String): Cursor? {
        return searchHistoryDataSource.updateSearchHistorySuggestion(query)
    }
}