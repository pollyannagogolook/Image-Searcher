package com.pollyannawu.gogolook.data.model

interface Repository {

    fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit)
}