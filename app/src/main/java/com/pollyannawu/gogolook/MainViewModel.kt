package com.pollyannawu.gogolook

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.model.Repository
import com.pollyannawu.gogolook.data.model.image_search.ITAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    companion object {
        const val TAG = "main viewModel"
        const val DEFAULT_LAYOUT_KEY = "default_layout"
        const val FAIL = "fail"
    }

    private val _defaultLayout = MutableStateFlow<String?>(null)
    val defaultLayout: Flow<String>
        get() = _defaultLayout.filterNotNull()


    private val _images = MutableStateFlow<PagingData<Hit>?>(null)
    val images: Flow<PagingData<Hit>>
        get() = _images.filterNotNull()


    private val _searchSuggestions = MutableStateFlow<Cursor?>(null)
    val searchSuggestions: Flow<Cursor>
        get() = _searchSuggestions.filterNotNull()

    private val _isLinear = MutableStateFlow<Boolean>(true)
    val isLinear: StateFlow<Boolean> = _isLinear.asStateFlow()


    fun loadAllImage() {
        viewModelScope.launch {
            try {
                repository.getImageBySearch("").cachedIn(viewModelScope).collect {
                    _images.value = it
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getDefaultLayoutByRemoteConfig() {
        viewModelScope.launch {
            _defaultLayout.value =
                repository.getDefaultLayoutByRemoteConfig(DEFAULT_LAYOUT_KEY, FAIL)
        }
    }

    fun getImagesBySearch(input: String) {
        viewModelScope.launch {
            try {
                _images.value = null
                repository.getImageBySearch(query = input).cachedIn(viewModelScope).collect {
                    _images.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updateSearchHistorySuggestion(query: String) {
        _searchSuggestions.value = repository.updateSearchHistorySuggestion(query)
    }

    fun saveSearchQuery(query: String) {
        repository.saveSearchQuery(query)
    }

    fun toggleLayout() {
        _isLinear.value = !_isLinear.value
    }
}


