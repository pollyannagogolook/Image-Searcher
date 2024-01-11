package com.pollyannawu.gogolook.data.model.image_search

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/** *
 * Author: Pollyanna Wu
 * Date: 2024-1-9
 * In this case, the PagingSource is the dataSource to fetch data from Pixabay API.
 * Thus we need to inject ApiService and query to the constructor.
 * **/
const val ITAG = "LoadImage"
class ImagePagingSource (
    private val service: ApiService,
    private val query: String
) : PagingSource<Int, Hit>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.searchImages(
                input = query,
                page = STARTING_PAGE_INDEX,
                per_page = params.loadSize
            )

            val photos = response.hits

            Log.i(ITAG, "load: ${photos.size}")
            LoadResult.Page(
                data = photos,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == photos.size) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

}