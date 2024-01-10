package com.pollyannawu.gogolook.data.model.remote_config

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepository @Inject constructor(private val remoteConfigManagerExt: RemoteConfig) {
    fun getDefaultLayout(key: String, fallback: String): String{
        return remoteConfigManagerExt.getString(key, fallback)
    }
}