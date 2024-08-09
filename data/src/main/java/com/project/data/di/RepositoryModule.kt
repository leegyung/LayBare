package com.project.data.di

import com.project.data.api.SearchAddressApi
import com.project.data.api.SearchImageApi
import com.project.data.api.SearchLandmarkApi
import com.project.data.repositoryImpl.SearchAddressRepositoryImpl
import com.project.data.repositoryImpl.SearchImageRepositoryImpl
import com.project.data.repositoryImpl.SearchLandmarkRepositoryImpl
import com.project.domain.repository.SearchAddressRepository
import com.project.domain.repository.SearchImageRepository
import com.project.domain.repository.SearchLandmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideImageListRepository(api: SearchImageApi) : SearchImageRepository {
        return SearchImageRepositoryImpl(api)
    }


    @Provides
    @Singleton
    fun provideLandmarkRepository(api: SearchLandmarkApi) : SearchLandmarkRepository {
        return SearchLandmarkRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAddressRepository(api: SearchAddressApi) : SearchAddressRepository {
        return SearchAddressRepositoryImpl(api)
    }


}
