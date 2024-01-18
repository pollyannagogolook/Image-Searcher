package com.pollyannawu.gogolook


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pollyannawu.gogolook.data.dataclass.ImageLayoutType
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

    private val viewModel: MainViewModel by viewModels()
    private var imageAdapter: ImageAdapter? = null
    private var lastQuery = ""
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()

        // set layout manager after getting remote setting value
        lifecycleScope.launch {
            viewModel.defaultLayout.collect { defaultLayout ->
                if (defaultLayout == DEFAULT_LAYOUT) {
                    binding.imageRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                } else {
                    binding.imageRecyclerview.layoutManager =
                        GridLayoutManager(this@MainActivity, GRID_COUNT_SPAN)
                }
            }
        }

        lifecycleScope.launch {

            viewModel.images.combine(viewModel.isLinear) { images, isLinear ->
               Pair(images, isLinear)
            }.collectLatest { (images, isLinear) ->

                showSuccessUI()
                val currentData: PagingData<ImageLayoutType> = if (isLinear) {
                    images.map { ImageLayoutType.LinearImage(it) }
                } else {
                    images.map { ImageLayoutType.GridImage(it) }
                }
                imageAdapter?.submitData(currentData)
            }

        }

        lifecycleScope.launch {
            viewModel.searchSuggestions.collect { cursor ->
                cursor?.let {
                    val searchHistoryCursorAdapter = SearchHistoryCursorAdapter(
                        cursor,
                        SearchHistoryCursorAdapter.OnClickListener { query ->
                            lastQuery = query
                            clickHistoryItem(query)
                        }
                    )
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
                    lastQuery = query
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.searchHistoryRecyclerview.visibility = View.VISIBLE
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
                viewModel.toggleLayout()


            } else {
                binding.imageRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                viewModel.toggleLayout()

            }
        }


        // when user swipe refresh, perform search again
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            performSearch(lastQuery)
        }

    }

    private fun init() {
        attachPagingAdapter()
        showLoadingUI()
        viewModel.getDefaultLayoutByRemoteConfig()
        viewModel.loadAllImage()
    }

    private fun attachPagingAdapter(){
        imageAdapter = ImageAdapter()
        binding.imageRecyclerview.adapter = imageAdapter
    }

    // ui state to different result type
    private fun showSuccessUI() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.errorHintText.visibility = View.GONE
        binding.errorHintLottie.visibility = View.GONE
        binding.imageRecyclerview.visibility = View.VISIBLE
    }

    private fun showLoadingUI() {
        binding.shimmerLayout.startShimmer()
        binding.imageRecyclerview.visibility = View.GONE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.errorHintText.visibility = View.GONE
        binding.errorHintLottie.visibility = View.GONE
    }

    private fun showErrorUI(content: String) {

        binding.shimmerLayout.stopShimmer()
        binding.imageRecyclerview.visibility = View.GONE
        binding.shimmerLayout.visibility = View.GONE

        binding.errorHintLottie.visibility = View.VISIBLE


        binding.errorHintText.visibility = View.VISIBLE
        binding.errorHintText.text = content
        binding.errorHintLottie.playAnimation()

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
        viewModel.getImagesBySearch(query)
        binding.searchHistoryRecyclerview.visibility = View.GONE
    }


    private fun saveSearchQuery(query: String) {
        viewModel.saveSearchQuery(query)
    }

    private fun clickHistoryItem(item: String) {
        showLoadingUI()
        binding.searchBar.clearFocus()
        binding.searchBar.setQuery(item, false)
        performSearch(item)
        hideKeyboard()
    }
}



