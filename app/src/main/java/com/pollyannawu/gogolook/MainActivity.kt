package com.pollyannawu.gogolook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import com.pollyannawu.gogolook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


    }
}



