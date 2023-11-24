package com.pollyannawu.gogolook.data.model

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.data.di.App
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(app: App) : RemoteDataSource {
    companion object{
        const val TAG = "repository"
        const val DEFAULT_LAYOUT = "default_layout"
    }
    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun getDefaultLayoutByRemoteConfig(defaultLayoutCallback: (String) -> Unit) {

        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        // get data to change config
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val defaultLayout = firebaseRemoteConfig.getString(DEFAULT_LAYOUT)
                    defaultLayoutCallback(defaultLayout)
                } else {
                    Log.i(TAG, "fetch remote config failed")
                }
            }
    }
}