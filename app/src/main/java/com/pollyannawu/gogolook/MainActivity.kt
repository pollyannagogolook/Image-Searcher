package com.pollyannawu.gogolook

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView.OnQueryTextListener
import android.widget.SimpleCursorAdapter
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pollyannawu.gogolook.data.dataclass.Result
import com.pollyannawu.gogolook.databinding.ActivityMainBinding
import com.pollyannawu.gogolook.searchbar.SearchHistoryCursorAdapter
import com.pollyannawu.gogolook.searchbar.SuggestionProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "mainactivity cursor"
        const val DEFAULT_LAYOUT = "linear"
        const val GRID_COUNT_SPAN = 2
    }

    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    var lastQuery = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val imageAdapter = ImageAdapter()

        binding.searchBar.clearFocus()


        // set layout manager after getting remote setting value
        lifecycleScope.launch {
            viewModel.defaultLayout.collect { defaultLayout ->
                if (defaultLayout == DEFAULT_LAYOUT) {
                    binding.imageRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                } else {
                    binding.imageRecyclerview.layoutManager =
                        GridLayoutManager(this@MainActivity, GRID_COUNT_SPAN)
                }
                binding.imageRecyclerview.adapter = imageAdapter
            }
        }

        lifecycleScope.launch {
            viewModel.result.collect { result ->
                when (result) {
                    is Result.Success -> {
                        imageAdapter.submitList(result.data)
                        showSuccessUI()
                    }

                    is Result.Loading -> showLoadingUI()
                    is Result.Error -> showErrorUI()
                    is Result.Fail -> showErrorUI()
                }


            }
        }

        lifecycleScope.launch {
            viewModel.searchSuggestions.collect { cursor ->
                cursor?.let {
                    val searchHistoryCursorAdapter = SearchHistoryCursorAdapter(cursor)
                    binding.searchHistoryRecyclerview.adapter = searchHistoryCursorAdapter
                }
            }
        }


        /**
         * watch user's input: When input changed, should query from SearchSuggestionsProvider
         * **/

        binding.searchBar.setOnQueryTextListener(object : OnQueryTextListener {

            // when user click search button on soft keyboard, call viewModel function to fetch data
            // while calling api, should show shimmer
            override fun onQueryTextSubmit(query: String?): Boolean {

                binding.searchBar.clearFocus()
                hideKeyboard()
                showLoadingUI()

                query?.let {
                    performSearch(query)
                    saveSearchQuery(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    updateSearchHistorySuggestion(newText)
                }
                return false
            }
        })

        binding.swapLayoutBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.imageRecyclerview.layoutManager =
                    GridLayoutManager(this@MainActivity, GRID_COUNT_SPAN)
            } else {
                binding.imageRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }

    }

    private fun showSuccessUI() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.errorHintImage.visibility = View.GONE
        binding.errorHintText.visibility = View.GONE
    }

    private fun showLoadingUI() {
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.errorHintImage.visibility = View.GONE
        binding.errorHintText.visibility = View.GONE
    }

    private fun showErrorUI() {
        binding.shimmerLayout.stopShimmer()
        binding.imageRecyclerview.visibility = View.GONE
        binding.shimmerLayout.visibility = View.GONE
        binding.errorHintImage.visibility = View.VISIBLE
        binding.errorHintText.visibility = View.VISIBLE

    }


    // when user click search, should hide the soft keyboard
    private fun hideKeyboard() {
        this.currentFocus.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun updateSearchHistorySuggestion(query: String) {
        viewModel.updateSearchHistorySuggestion(query)
    }


    private fun performSearch(query: String) {
        viewModel.getImagesFromPixabayAPI(query)
    }


    private fun saveSearchQuery(query: String) {
        viewModel.saveSearchQuery(query)
    }
}



