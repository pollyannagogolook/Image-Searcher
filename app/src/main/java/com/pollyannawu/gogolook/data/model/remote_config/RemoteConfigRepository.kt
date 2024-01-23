package com.pollyannawu.gogolook.data.model.remote_config

import javax.inject.Inject
import javax.inject.Singleton

// singleton 不用每個都用


class RemoteConfigRepository @Inject constructor(private val remoteConfigDataSource: RemoteConfig) {
    fun getDefaultLayout(key: String, fallback: String): String{
        return remoteConfigDataSource.getString(key, fallback)
    }
}