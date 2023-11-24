package com.pollyannawu.gogolook

import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
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


        // set layout manager after remote setting
        lifecycleScope.launch {
            viewModel.defaultLayout.collect{ defaultLayout ->
                if (defaultLayout == DEFAULT_LAYOUT){
                    binding.imageRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                }
                else{
                    binding.imageRecyclerview.layoutManager = GridLayoutManager(this@MainActivity, GRID_COUNT_SPAN)
                }
            }
        }


    }
}



