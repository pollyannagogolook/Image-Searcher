package com.pollyannawu.gogolook.data.model.search_history


import android.database.Cursor
import javax.inject.Inject


class SearchHistoryRepository @Inject constructor(private val searchHistoryDataSource: SearchHistoryDataSource){
    fun insertSearchQuery(query: String){
        searchHistoryDataSource.insertSearchHistory(query)

    }
    fun getSearchHistorySuggestion(query: String): Cursor? {
        return searchHistoryDataSource.getSearchHistorySuggestion(query)
    }
}