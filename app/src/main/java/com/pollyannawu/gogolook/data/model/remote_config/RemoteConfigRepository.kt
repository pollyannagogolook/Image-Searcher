package com.pollyannawu.gogolook.data.model.remote_config

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepository @Inject constructor(remoteConfigManagerExt: FirebaseRemoteConfigManagerExt) {
    fun getDefaultLayout(defaultLayoutCallback: (String) -> Unit){}
}