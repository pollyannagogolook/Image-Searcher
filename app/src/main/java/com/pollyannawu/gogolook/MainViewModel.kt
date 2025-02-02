package com.pollyannawu.gogolook

import android.app.SearchManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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




    private val _pagingFlow = MutableStateFlow<PagingData<Hit>>(PagingData.empty())
    val pagingFlow : StateFlow<PagingData<Hit>> = _pagingFlow

    private val _searchSuggestions = mutableStateOf<List<String>>(emptyList())
    val searchSuggestions: State<List<String>> = _searchSuggestions

    private val _isLinear = mutableStateOf<Boolean>(true)
    val isLinear: State<Boolean> = _isLinear


    private val _isSearch = mutableStateOf<Boolean>(true)
    val isSearch: State<Boolean> = _isSearch


    private val _searchText = mutableStateOf<String>("")


    init {
        getDefaultLayoutByRemoteConfig()
        loadAllImage()
    }

    private fun loadAllImage() {
        viewModelScope.launch {
            try {
                repository.getImageBySearch("").cachedIn(viewModelScope).collect {
                    _pagingFlow.value = it
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun turnOnSearch(){
        _isSearch.value = true
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
                _pagingFlow.value = repository.getImageBySearch(query = input).cachedIn(viewModelScope).first()
            } catch (e: Exception) {
              e.printStackTrace()
            }
        }
    }


    fun getSearchHistorySuggestion(query: String) {
        val searchHistoryCursor = repository.getSearchHistorySuggestion(query)
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
        _searchSuggestions.value = list

    }

    fun saveSearchQuery(query: String) {
        repository.saveSearchQuery(query)
    }

    fun changeLayout():Boolean {
        _isLinear.value = !_isLinear.value
        return _isLinear.value
    }
}


