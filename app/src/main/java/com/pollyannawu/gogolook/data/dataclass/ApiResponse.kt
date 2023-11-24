package com.pollyannawu.gogolook.data.dataclass

import com.squareup.moshi.Json

data class ApiResponse(

    val total: Int,
    val totalHits: Int,
    val hits: List<Hit>
)

data class Hit(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int,
    val webformatHeight: Int,
    val largeImageURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val views: Int,
    val downloads: Int,
    val collections: Int,
    val likes: Int,
    val comments: Int,
    @Json(name = "user_id")
    val userId: Int,
    val user: String,
    val userImageURL: String
)