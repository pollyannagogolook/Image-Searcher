package com.pollyannawu.gogolook

import android.app.SearchManager
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
import kotlinx.coroutines.delay
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
        const val LINEAR = "linear"
    }




    private val _images = MutableStateFlow<PagingData<Hit>?>(null)
    val images: Flow<PagingData<Hit>>
        get() = _images.filterNotNull()


    private val _searchSuggestions = MutableStateFlow<List<String>>(emptyList())
    val searchSuggestions: Flow<List<String>>
        get() = _searchSuggestions.filterNotNull()

    private val _isLinear = MutableStateFlow<Boolean>(true)
    val isLinear: StateFlow<Boolean> = _isLinear.asStateFlow()

    private val _isSearch = MutableStateFlow<Boolean>(true)
    val isSearch: StateFlow<Boolean> = _isSearch.asStateFlow()

    private val _searchText = MutableStateFlow<String>("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    init {
        getDefaultLayoutByRemoteConfig()
        loadAllImage()
    }

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

    fun turnOnSearch(){
        _isSearch.value = true
        _images.value = PagingData.empty()
    }
    fun turnOffSearch(){
        _isSearch.value = false
    }

    private fun getDefaultLayoutByRemoteConfig() {
        viewModelScope.launch {
            _isLinear.value = repository.getDefaultLayoutByRemoteConfig(DEFAULT_LAYOUT_KEY, FAIL) == LINEAR
        }
    }

    fun getImagesBySearch(input: String) {
        _searchText.value = input
        viewModelScope.launch {
            try {
                _images.value = repository.getImageBySearch(query = input).cachedIn(viewModelScope).first()
            } catch (e: Exception) {
              e.printStackTrace()
            }
        }
    }


    fun updateSearchHistorySuggestion(query: String) {
        val searchHistoryCursor = repository.updateSearchHistorySuggestion(query)
        val list = ArrayList<String>()

        // transfer cursor content to list
        searchHistoryCursor?.let { cursor ->
            while (cursor.moveToNext()) {
                list.add(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1)
                    )
                )
            }
        }
        _searchSuggestions.value = list

    }

    fun saveSearchQuery(query: String) {
        repository.saveSearchQuery(query)
    }

    fun toggleLayout() {
        _isLinear.value = !_isLinear.value
    }
}


