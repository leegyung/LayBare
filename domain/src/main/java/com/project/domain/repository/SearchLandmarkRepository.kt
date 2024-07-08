package com.project.domain.repository

import com.project.domain.entity.LandmarkEntity
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface SearchLandmarkRepository {
    suspend fun searchLandmark(apiKey : String, image : String, maxResult : Int) : Flow<ApiResult<LandmarkEntity>>
}