package com.pollyannawu.gogolook

import android.content.SearchRecentSuggestionsProvider

class SuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object{
        const val AUTHORITY = "com.pollyannawu.gogolook.SuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}