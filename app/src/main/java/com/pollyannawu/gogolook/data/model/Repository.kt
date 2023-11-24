package com.pollyannawu.gogolook.data.model

import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.Result

interface Repository {

    fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit)

    suspend fun getImagesFromPixabayAPI(input: String): Result<List<Hit>>
}