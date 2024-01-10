package com.pollyannawu.gogolook.data.model.remote_config

import android.util.Log
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.get
import com.pollyannawu.gogolook.data.model.RemoteDataSourceImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigManagerExt @Inject constructor(): RemoteConfig {

    init {
        initiateRemoteConfig()
    }

    private fun initiateRemoteConfig(){
       val builder = FirebaseRemoteConfigSettings.Builder()

        if(BuildConfig.DEBUG){
            builder.minimumFetchIntervalInSeconds = 0L
        }
        val configSettings = builder.build()
        FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(configSettings)
    }

    override fun getString(key: String, fallback: String): String {
        return FirebaseRemoteConfig.getInstance()[key].run{
            try {
                asString()
            } catch (e: IllegalArgumentException) {
                fallback
            }
        }
    }
}