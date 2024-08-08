package com.project.data.di

import com.project.data.api.SearchAddressApi
import com.project.data.api.SearchImageApi
import com.project.data.api.SearchLandmarkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val mRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideSearchImageApi(): SearchImageApi {
        return mRetrofit
            .baseUrl("https://www.googleapis.com/")
            .build()
            .create(SearchImageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchLandmarkApi(): SearchLandmarkApi {
        return mRetrofit
            .baseUrl("https://vision.googleapis.com/")
            .build()
            .create(SearchLandmarkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchAddressApi(): SearchAddressApi {
        return mRetrofit
            .baseUrl("https://maps.googleapis.com/")
            .build()
            .create(SearchAddressApi::class.java)
    }
}