package com.pollyannawu.gogolook.searchbar

import android.content.SearchRecentSuggestionsProvider

class SuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object{
        const val AUTHORITY = "com.pollyannawu.gogolook.searchbar.SuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}