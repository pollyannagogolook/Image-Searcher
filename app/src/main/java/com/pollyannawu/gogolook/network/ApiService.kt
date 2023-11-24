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


private const val HOST_NAME = "pixabay.com"
private const val BASE_URL = "https://$HOST_NAME/api"
private const val API_KEY = "key"

internal val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()


// client for retroit
private val client = OkHttpClient.Builder()
    .addInterceptor{chain ->
        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()
        val request = original.newBuilder().url(url).build()
        chain.proceed(request)
    }.build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()


interface ApiService {
    @GET("/")
    suspend fun searchImages (
        @Query("q")input: String,
        @Query("image_type")imageType: String = "photo"
    ): Response<ApiResponse>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */

object GogolookApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}