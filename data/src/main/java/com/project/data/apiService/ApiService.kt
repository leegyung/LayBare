package com.project.data.apiService

import com.project.data.api.SearchImageApi
import com.project.data.api.SearchLandmarkApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton


object ApiService {
    fun getSearchImageApi() : SearchImageApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SearchImageApi::class.java)
    }

    fun getSearchLandmarkApi() : SearchLandmarkApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://vision.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SearchLandmarkApi::class.java)
    }
}