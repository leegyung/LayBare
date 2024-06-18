package com.project.data.apiService

import com.project.data.api.SearchImageApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


object ApiService {
    private val RETROFIT_BUILDER = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getSearchImageApi() : SearchImageApi {
        return RETROFIT_BUILDER.create(SearchImageApi::class.java)
    }
}