package com.pollyannawu.gogolook.data.model.remote_config

import android.util.Log
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.get
import com.pollyannawu.gogolook.R
import javax.inject.Inject
import javax.inject.Singleton

// data sourceｒ
class RemoteConfigDataSource @Inject constructor(): RemoteConfig {
    companion object {
        const val TAG = "FirebaseRemoteConfigManagerExt"
        const val DEFAULT_LAYOUT = "default_layout"
        const val FALLBACK = "fallback"
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

        // set update listener
        firebaseRemoteConfig?.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate : ConfigUpdate) {

                if (configUpdate.updatedKeys.contains(DEFAULT_LAYOUT)) {
                    firebaseRemoteConfig?.activate()?.addOnCompleteListener {
                        getString(DEFAULT_LAYOUT, FALLBACK)
                    }
                }
            }

            override fun onError(error : FirebaseRemoteConfigException) {
                error.printStackTrace()
            }
        })

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