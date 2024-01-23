package com.pollyannawu.gogolook.data.model.search_history

import android.app.Application
import android.app.SearchManager
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.SearchRecentSuggestions
import com.pollyannawu.gogolook.searchbar.SuggestionProvider
import dagger.hilt.EntryPoint
import javax.inject.Inject
import javax.inject.Singleton


class SearchHistoryDataSource @Inject constructor(private val app: Application) {

    fun updateSearchHistorySuggestion(query: String): Cursor? {
        // use content provider to access query history
        val uri = Uri.parse("content://${SuggestionProvider.AUTHORITY}/search_suggest_query")
        val projection = arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)
        val selection = "${SearchManager.SUGGEST_COLUMN_TEXT_1} LIKE ?"
        val selectionArgs = arrayOf("%$query%")


        // when get history data, submit to cursorAdapter
        return app.contentResolver.query(uri, projection, selection, selectionArgs, null)
    }


    fun saveSearchQuery(query: String) {
        val suggestions = SearchRecentSuggestions(
            app,
            SuggestionProvider.AUTHORITY,
            SuggestionProvider.MODE
        )
        suggestions.saveRecentQuery(query, null)
    }
}