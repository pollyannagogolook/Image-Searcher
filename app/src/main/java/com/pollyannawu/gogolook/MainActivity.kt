package com.pollyannawu.gogolook

import android.content.Context
import android.graphics.ColorSpace.Adaptation
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SearchViewBindingAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pollyannawu.gogolook.data.dataclass.Result
import com.pollyannawu.gogolook.data.dataclass.succeeded
import com.pollyannawu.gogolook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object{
        const val TAG = "main activity"
        const val DEFAULT_LAYOUT = "linear"
        const val GRID_COUNT_SPAN = 6
    }
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val imageAdapter = ImageAdapter()

        binding.searchBar.clearFocus()


        // set layout manager after getting remote setting value
        lifecycleScope.launch {
            viewModel.defaultLayout.collect{ defaultLayout ->
                if (defaultLayout == DEFAULT_LAYOUT){
                    binding.imageRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                }
                else {
                    binding.imageRecyclerview.layoutManager = GridLayoutManager(this@MainActivity, GRID_COUNT_SPAN)
                }
                binding.imageRecyclerview.adapter = imageAdapter





            }


        }

        // if result is success
        lifecycleScope.launch {

            viewModel.succeedResultList.collect{ result ->
                binding.shimmerLayout.stopShimmer()
                imageAdapter.submitList(result)
            }
        }

        // if result is loading
        lifecycleScope.launch {
            viewModel.loadingResult.collect{ isLoading ->
                if (isLoading){

                    binding.shimmerLayout.startShimmer()
                }
            }
        }

        // if result is error, show hint


        /**
         * watch user's input: When input changed, should query from SearchSuggestionsProvider
         * **/

        binding.searchBar.setOnQueryTextListener(object : OnQueryTextListener{

            // when user click search button on soft keyboard, call viewModel function to fetch data
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.searchBar.clearFocus()
                hideKeyboard()
                query?.let{ viewModel.getImagesFromPixabayAPI(it)}

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })

    }


    // when user click search, should hide the soft keyboard
    private fun hideKeyboard(){
        this.currentFocus.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}



