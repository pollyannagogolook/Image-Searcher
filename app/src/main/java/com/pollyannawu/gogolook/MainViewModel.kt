package com.pollyannawu.gogolook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.Result
import com.pollyannawu.gogolook.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _defaultLayout = MutableSharedFlow<String>()
    var defaultLayout: SharedFlow<String> = _defaultLayout.asSharedFlow()

    private val _succeedResultList = MutableSharedFlow<List<Hit>>()
    var succeedResultList: SharedFlow<List<Hit>> = _succeedResultList.asSharedFlow()

    private val _failResult = MutableSharedFlow<Boolean>()
    var failResult: SharedFlow<Boolean> = _failResult.asSharedFlow()

    private val _loadingResult = MutableSharedFlow<Boolean>()
    var loadingResult: SharedFlow<Boolean> = _loadingResult.asSharedFlow()





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
                when(val imageResult = repository.getImagesFromPixabayAPI(input)){
                    is Result.Success -> _succeedResultList.emit(imageResult.data)
                    is Result.Fail -> _failResult.emit(true)
                    is Result.Error -> _failResult.emit(true)
                    is Result.Loading -> _loadingResult.emit(true)
                }

            }
        }
    }


}