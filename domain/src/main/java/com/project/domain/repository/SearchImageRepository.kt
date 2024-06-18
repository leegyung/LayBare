package com.project.domain.repository

import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface SearchImageRepository {
    suspend fun getImageList(apiKey : String, searchEngine : String, keyword : String, page : Int, size : Int) : Flow<ApiResult<SearchImageResultEntity>>
}