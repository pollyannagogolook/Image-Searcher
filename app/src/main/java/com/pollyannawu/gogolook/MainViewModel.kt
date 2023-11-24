package com.pollyannawu.gogolook

import androidx.lifecycle.ViewModel
import com.pollyannawu.gogolook.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(repository: Repository): ViewModel() {


}