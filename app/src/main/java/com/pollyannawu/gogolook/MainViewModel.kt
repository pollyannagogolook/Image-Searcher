package com.pollyannawu.gogolook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _defaultLayout = MutableSharedFlow<String>()
    var defaultLayout: SharedFlow<String> = _defaultLayout.asSharedFlow()

    init {
        getDefaultLayoutByRemoteConfig()
    }
    private fun getDefaultLayoutByRemoteConfig() {
        repository.getDefaultLayoutByRemoteConfig { layout ->
            viewModelScope.launch {
                _defaultLayout.emit(layout)
            }
        }
    }

    fun getImagesFromPixabayAPI(input: String) {
        viewModelScope.launch {
            repository.getImagesFromPixabayAPI(input)
        }
    }


}