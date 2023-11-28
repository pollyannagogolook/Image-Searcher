package com.pollyannawu.gogolook.data.model

import android.app.Application
import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.BaseColumns
import android.provider.SearchRecentSuggestions
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.Result
import com.pollyannawu.gogolook.network.GogolookApi
import com.pollyannawu.gogolook.searchbar.SuggestionProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(private val app: Application) : RemoteDataSource {
    companion object {
        const val TAG = "remoteDataSource"
        const val DEFAULT_LAYOUT = "default_layout"
    }

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    // check network is available while using retrofit
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
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
                if (task.isSuccessful) {
                    val defaultLayout = firebaseRemoteConfig.getString(DEFAULT_LAYOUT)
                    defaultLayoutCallback(defaultLayout)
                } else {
                    Log.i(TAG, "fetch remote config failed")
                }
            }
    }

    override suspend fun getImagesFromPixabayAPI(input: String): Result<List<Hit>> {

        // CoroutineExceptionHandler for handling exceptions
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i(TAG, "Error fetching data: ${throwable}")
            Result.Fail("We cannot get image due to some network problems: $throwable")
        }

        // check network connection before fetching data by retrofit
        if (!isNetworkAvailable()) {
            return Result.Fail(app.getString(R.string.offline_mode))
        }
        return withContext(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = GogolookApi.retrofitService.searchImages(input = input)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it.hits)
                    } ?: Result.Fail(app.getString(R.string.no_data_can_be_received_from_server))

                } else {
                    Log.i(TAG, "Error fetching data: ${response.errorBody()?.string()}")
                    Result.Fail("Error fetching data: ${response.errorBody()?.string()}")

                }
            } catch (e: Exception) {
                Log.i(TAG, "Error fetching data: ${e.message}")
                Result.Fail("Exception Occurred: ${e.message}")
            }
        }

    }

    override fun updateSearchHistorySuggestion(query: String): Cursor? {
        // use content provider to access query history
        val uri = Uri.parse("content://${SuggestionProvider.AUTHORITY}/search_suggest_query")
        val projection = arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)
        val selection = "${SearchManager.SUGGEST_COLUMN_TEXT_1} LIKE ?"
        val selectionArgs = arrayOf("%$query%")

        Log.i(TAG, "selection arg: $query")

        // when get history data, submit to cursorAdapter
        return app.contentResolver.query(uri, projection, selection, selectionArgs, null)
    }


    override fun saveSearchQuery(query: String) {
        val suggestions = SearchRecentSuggestions(
            app,
            SuggestionProvider.AUTHORITY,
            SuggestionProvider.MODE
        )
        suggestions.saveRecentQuery(query, null)
    }
}