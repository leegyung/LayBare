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

    @Provides
    @Singleton
    fun provideSearchImageApi(): SearchImageApi {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchImageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchLandmarkApi(): SearchLandmarkApi {
        return Retrofit.Builder()
            .baseUrl("https://vision.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchLandmarkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchAddressApi(): SearchAddressApi {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchAddressApi::class.java)
    }
}