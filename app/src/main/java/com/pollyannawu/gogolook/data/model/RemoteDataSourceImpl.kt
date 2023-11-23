package com.pollyannawu.gogolook.data.model

import com.pollyannawu.gogolook.data.di.App
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(app: App) : RemoteDataSource {
}