package com.pollyannawu.gogolook


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pollyannawu.gogolook.compose.ImageSearcherApp
import com.pollyannawu.gogolook.data.dataclass.Result
import com.pollyannawu.gogolook.data.model.image_search.ITAG
import com.pollyannawu.gogolook.databinding.ActivityMainBinding
import com.pollyannawu.gogolook.searchbar.SearchHistoryCursorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    companion object {
        const val TAG = "mainactivity cursor"
        const val DEFAULT_LAYOUT = "linear"
        const val GRID_COUNT_SPAN = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ImageSearcherApp()
        }


    }

//    private fun init() {
//        showLoadingUI()
//        viewModel.getDefaultLayoutByRemoteConfig()
//        viewModel.loadAllImage()
//    }
//
//    // ui state to different result type
//    private fun showSuccessUI() {
//        binding.shimmerLayout.stopShimmer()
//        binding.shimmerLayout.visibility = View.GONE
//        binding.errorHintText.visibility = View.GONE
//        binding.errorHintLottie.visibility = View.GONE
//        binding.imageRecyclerview.visibility = View.VISIBLE
//    }
//
//    private fun showLoadingUI() {
//        binding.shimmerLayout.startShimmer()
//        binding.imageRecyclerview.visibility = View.GONE
//        binding.shimmerLayout.visibility = View.VISIBLE
//        binding.errorHintText.visibility = View.GONE
//        binding.errorHintLottie.visibility = View.GONE
//    }
//
//    private fun showErrorUI(content: String) {
//
//        binding.shimmerLayout.stopShimmer()
//        binding.imageRecyclerview.visibility = View.GONE
//        binding.shimmerLayout.visibility = View.GONE
//
//        binding.errorHintLottie.visibility = View.VISIBLE
//
//
//        binding.errorHintText.visibility = View.VISIBLE
//        binding.errorHintText.text = content
//        binding.errorHintLottie.playAnimation()
//
//    }
//
//
//    // when user click search, should hide the soft keyboard
//    private fun hideKeyboard() {
//        this.currentFocus.let { view ->
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
//        }
//    }
//
//    private fun updateSearchHistorySuggestion(query: String) {
//        viewModel.updateSearchHistorySuggestion(query)
//    }
//
//
//    private fun performSearch(query: String) {
//        viewModel.getImagesBySearch(query)
//        Log.i(ITAG, "perform search: $query")
//        binding.searchHistoryRecyclerview.visibility = View.GONE
//    }
//
//
//    private fun saveSearchQuery(query: String) {
//        viewModel.saveSearchQuery(query)
//    }
//
//    private fun clickHistoryItem(item: String) {
//        showLoadingUI()
//        binding.searchBar.clearFocus()
//        binding.searchBar.setQuery(item, false)
//        performSearch(item)
//        hideKeyboard()
//    }
}



