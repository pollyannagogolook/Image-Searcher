package com.pollyannawu.gogolook

import android.graphics.ColorSpace.Adaptation
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pollyannawu.gogolook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
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

        // record user latest input
        var userInput = ""


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

        // watch user's input: When input changed, should query from SearchSuggestionsProvider
        binding.searchInput.doAfterTextChanged {editable ->
            userInput = editable.toString()
        }


        // when user click search button on soft keyboard, call viewModel function to fetch data
        binding.searchInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                viewModel.getImagesFromPixabayAPI(userInput)
                true
            }else{
                false
            }
        }
    }
}



