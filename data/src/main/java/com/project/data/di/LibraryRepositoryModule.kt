package com.project.data.di

import com.project.data.repositoryImpl.library.ImageRecognitionRepositoryImpl
import com.project.data.repositoryImpl.library.TextRecognitionRepositoryImpl
import com.project.domain.repository.library.ImageRecognitionRepository
import com.project.domain.repository.library.TextRecognitionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LibraryRepositoryModule {

    @Singleton
    @Provides
    fun provideTextExtractLibraryRepository() : TextRecognitionRepository {
        return TextRecognitionRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideImageRecognitionRepository() : ImageRecognitionRepository {
        return ImageRecognitionRepositoryImpl()
    }

}