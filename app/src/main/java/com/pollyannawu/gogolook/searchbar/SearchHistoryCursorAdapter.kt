package com.pollyannawu.gogolook.searchbar

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.pollyannawu.gogolook.databinding.SearchHistoryViewholderBinding

class SearchHistoryCursorAdapter(private var cursor: Cursor, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<SearchHistoryCursorAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val binding: SearchHistoryViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchItem: String) {
            binding.searchItem = searchItem
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (data: String) -> Unit) {
        fun onClick(data: String) = clickListener(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchHistoryViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return cursor.count ?: 0
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (cursor.moveToPosition(position)) {
            val text =
                cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))

            text?.let {
                holder.bind(text)
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(text)
                }
            }


        }

    }




}