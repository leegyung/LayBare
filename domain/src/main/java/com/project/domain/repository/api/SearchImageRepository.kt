package com.project.domain.repository.api

import androidx.paging.PagingData
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import kotlinx.coroutines.flow.Flow

interface SearchImageRepository {
    suspend fun searchImage(apiKey : String, searchEngine : String, keyword : String, page : Int, size : Int) : SearchImageResultEntity
    suspend fun getSearchImagePagingSource(apiKey : String, searchEngine : String, keyword : String) : Flow<PagingData<ImageEntity>>
}