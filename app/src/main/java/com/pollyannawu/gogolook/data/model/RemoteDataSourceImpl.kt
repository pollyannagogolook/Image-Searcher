package com.pollyannawu.gogolook.data.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.di.App
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(private val app: Application) : RemoteDataSource {
    companion object{
        const val TAG = "repository"
        const val DEFAULT_LAYOUT = "default_layout"
    }
    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    // check network is available while using retrofit
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network)?: return false

        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

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

    override fun getImagesFromPixabayAPI():Result<List<Hit>> {
        if (! )
    }
}