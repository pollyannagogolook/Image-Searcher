package com.pollyannawu.gogolook.searchbar

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import androidx.databinding.DataBindingUtil
import com.pollyannawu.gogolook.databinding.SearchHistoryViewholderBinding

class SearchHistoryCursorAdapter(context: Context, cursor: Cursor): CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val binding = SearchHistoryViewholderBinding.inflate(LayoutInflater.from(context), parent, false)
        return binding.root
    }

    override fun bindView(view: View, context: Context, cursor: Cursor?) {
        val binding = DataBindingUtil.getBinding<SearchHistoryViewholderBinding>(view)
        val searchItem = cursor?.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))

        binding?.let { binding.searchItem = searchItem }
    }

}