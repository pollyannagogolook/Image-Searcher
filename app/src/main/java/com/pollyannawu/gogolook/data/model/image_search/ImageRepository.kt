package com.pollyannawu.gogolook.data.model.image_search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.network.ApiService

import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

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