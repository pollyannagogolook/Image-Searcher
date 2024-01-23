package com.pollyannawu.gogolook

import android.app.SearchManager
import android.database.Cursor
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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




    var images = MutableStateFlow<PagingData<Hit>>(PagingData.empty())
    private val _images = MutableStateFlow<PagingData<Hit>>(PagingData.empty())

    var searchSuggestions = mutableStateOf<List<String>>(emptyList())
    private set

    var isLinear = mutableStateOf<Boolean>(true)
    private set

    var isSearch = mutableStateOf<Boolean>(true)
    private set

    var searchText = mutableStateOf<String>("")
    private set

    init {
        getDefaultLayoutByRemoteConfig()
        loadAllImage()
    }

    private fun loadAllImage() {
        viewModelScope.launch {
            try {
                repository.getImageBySearch("").cachedIn(viewModelScope).collect {
                    images.value = it
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun turnOnSearch(){
        isSearch.value = true
//        _images.value = PagingData.empty()
    }
    fun turnOffSearch(){
        isSearch.value = false
    }

    private fun getDefaultLayoutByRemoteConfig() {
        viewModelScope.launch {
            isLinear.value = repository.getDefaultLayoutByRemoteConfig(DEFAULT_LAYOUT_KEY, FAIL) == LINEAR
        }
    }

    fun getImagesBySearch(input: String) {
        searchText.value = input
        viewModelScope.launch {
            try {
                images.value = repository.getImageBySearch(query = input).cachedIn(viewModelScope).first()
            } catch (e: Exception) {
              e.printStackTrace()
            }
        }
    }


    fun updateSearchHistorySuggestion(query: String) {
        val searchHistoryCursor = repository.updateSearchHistorySuggestion(query)
        val list = ArrayList<String>()

        // transfer cursor content to list
        searchHistoryCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                list.add(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1)
                    )
                )
            }
        }
        searchSuggestions.value = list

    }

    fun saveSearchQuery(query: String) {
        repository.saveSearchQuery(query)
    }

    fun toggleLayout() {
        isLinear.value = !isLinear.value
    }
}


