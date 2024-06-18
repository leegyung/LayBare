package com.project.data.di

import com.project.data.apiService.ApiService
import com.project.data.repositoryImpl.SearchImageRepositoryImpl
import com.project.domain.repository.SearchImageRepository
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
    fun provideApiService() : ApiService {
        return ApiService
    }


    @Provides
    @Singleton
    fun provideImageListRepository(apiService: ApiService) : SearchImageRepository {
        return SearchImageRepositoryImpl(apiService)
    }
}