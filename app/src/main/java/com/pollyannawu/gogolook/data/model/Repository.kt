package com.pollyannawu.gogolook.data.model

import com.pollyannawu.gogolook.data.dataclass.Hit

interface Repository {

    fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit)

    fun getImagesFromPixabayAPI(hitsCallback: (List<Hit>) -> Unit)
}