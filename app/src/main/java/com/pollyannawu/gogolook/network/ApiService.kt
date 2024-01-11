package com.pollyannawu.gogolook.network

import com.pollyannawu.gogolook.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import com.pollyannawu.gogolook.data.dataclass.ApiResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



interface ApiService {
    @GET(".")
    suspend fun searchImages (
        @Query("key")apiKey: String = BuildConfig.API_KEY,
        @Query("q")input: String,
        @Query("image_type")imageType: String = "photo",
        @Query("page")page: Int,
        @Query("per_page")per_page: Int
    ): ApiResponse
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */

