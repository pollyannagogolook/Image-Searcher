package com.pollyannawu.gogolook.data.model.remote_config

import android.util.Log
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.get
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.data.model.RemoteDataSourceImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigManagerExt @Inject constructor(): RemoteConfig {
    companion object {
        const val TAG = "FirebaseRemoteConfigManagerExt"
    }

    private var firebaseRemoteConfig: FirebaseRemoteConfig? = null
    init {
        initiateRemoteConfig()
    }

    private fun initiateRemoteConfig(){
        if (firebaseRemoteConfig !== null) {
            return
        }

       firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set default value
        firebaseRemoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)

        // fetch remote config
        firebaseRemoteConfig?.fetchAndActivate()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "FirebaseRemoteConfigManagerExt fetchAndActivate success")
            } else {
                Log.d(TAG, "FirebaseRemoteConfigManagerExt fetchAndActivate failed")
            }
        }
    }

    override fun getString(key: String, fallback: String): String {
        val config = firebaseRemoteConfig?.get(key)?: FirebaseRemoteConfig.getInstance().get(key)

        return try {
            config.asString()
        }catch (e: IllegalArgumentException){
            fallback
        }
    }
}