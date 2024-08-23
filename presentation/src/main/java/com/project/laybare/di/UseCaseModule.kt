package com.project.laybare.di

import com.project.domain.repository.api.SearchAddressRepository
import com.project.domain.repository.api.SearchImageRepository
import com.project.domain.repository.api.SearchLandmarkRepository
import com.project.domain.repository.library.TextRecognitionRepository
import com.project.domain.usecase.ExtractTextEntityUseCase
import com.project.domain.usecase.ExtractTextUseCase
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

    @Provides
    @Singleton
    fun provideExtractTextUseCase(repository: TextRecognitionRepository) : ExtractTextUseCase {
        return ExtractTextUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideExtractTextEntityUseCase(repository: TextRecognitionRepository) : ExtractTextEntityUseCase {
        return ExtractTextEntityUseCase(repository)
    }

}