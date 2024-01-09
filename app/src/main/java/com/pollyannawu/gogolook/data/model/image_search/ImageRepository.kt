package com.pollyannawu.gogolook.data.model.image_search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(private val service: ApiService){
    fun getAllImages(): Flow<PagingData<Hit>> {
        return getImageBySearch("")
    }

    fun getImageBySearch(input: String): Flow<PagingData<Hit>>{
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { ImagePagingSource(
                service,
                query = input

            ) }
        ).flow
    }

    companion object{
        private const val NETWORK_PAGE_SIZE = 25
        private const val STARTING_PAGE_INDEX = 1
    }
}