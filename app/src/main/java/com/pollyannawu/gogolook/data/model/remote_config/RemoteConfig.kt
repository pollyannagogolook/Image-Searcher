package com.pollyannawu.gogolook.data.model.remote_config

interface RemoteConfig {
    fun getString(key: String, fallback: String): String

}