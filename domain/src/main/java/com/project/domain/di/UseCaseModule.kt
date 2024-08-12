package com.project.domain.di

import com.project.domain.repository.SearchAddressRepository
import com.project.domain.repository.SearchImageRepository
import com.project.domain.repository.SearchLandmarkRepository
import com.project.domain.usecase.SearchAddressUseCase
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.usecase.SearchLandmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun providePictureListUseCase(repository : SearchImageRepository): SearchImageUseCase {
        return SearchImageUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLandmarkUseCase(repository : SearchLandmarkRepository): SearchLandmarkUseCase {
        return SearchLandmarkUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddressUseCase(repository: SearchAddressRepository) : SearchAddressUseCase {
        return SearchAddressUseCase(repository)
    }

}