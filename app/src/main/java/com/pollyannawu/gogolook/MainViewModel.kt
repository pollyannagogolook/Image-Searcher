package com.pollyannawu.gogolook

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.Result
import com.pollyannawu.gogolook.data.dataclass.succeeded
import com.pollyannawu.gogolook.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    companion object{
        const val TAG = "main viewModel"
    }

    private val _defaultLayout = MutableSharedFlow<String>()
    var defaultLayout: SharedFlow<String> = _defaultLayout.asSharedFlow()

    private val _result = MutableSharedFlow<Result<List<Hit>>>()
    var result: SharedFlow<Result<List<Hit>>> = _result.asSharedFlow()

    private val _searchSuggestions = MutableStateFlow<Cursor?>(null)
    val searchSuggestions: StateFlow<Cursor?> = _searchSuggestions.asStateFlow()

    private val _isLinear = MutableStateFlow<Boolean>(true)
    val isLinear: StateFlow<Boolean> = _isLinear.asStateFlow()


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
            withContext(Dispatchers.Main) {
                val imageResult = repository.getImagesFromPixabayAPI(input)
                Log.i(TAG, "result: ${imageResult}")
                _result.emit(imageResult)

            }
        }
    }


    fun updateSearchHistorySuggestion(query: String){
        _searchSuggestions.value = repository.updateSearchHistorySuggestion(query)
    }

    fun saveSearchQuery(query: String){
        repository.saveSearchQuery(query)
    }

    fun toggleLayout(){
       _isLinear.value = !_isLinear.value
    }
}


