package com.pollyannawu.gogolook.data.model.image_search

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.pollyannawu.gogolook.MainActivity.Companion.TAG
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

class ImageRepository @Inject constructor(private val service: ApiService){

    fun getImageBySearch(input: String): Flow<PagingData<Hit>>{
        val pagerFlow = Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { ImagePagingSource(
                service,
                query = input

            ) }
        ).flow

        return pagerFlow
    }

    companion object{
        private const val NETWORK_PAGE_SIZE = 20
        private const val STARTING_PAGE_INDEX = 1
    }
}