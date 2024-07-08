package com.project.domain.usecase

import com.project.domain.entity.LandmarkEntity
import com.project.domain.repository.SearchLandmarkRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchLandmarkUseCase@Inject constructor(private val mRepository : SearchLandmarkRepository) {
    suspend fun getLandmarkLocation(apiKey : String, image : String, maxResult : Int) : Flow<ApiResult<LandmarkEntity>> {
        return mRepository.searchLandmark(apiKey, image, maxResult)
    }
}