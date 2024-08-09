package com.project.domain.usecase

import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.repository.SearchLandmarkRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SearchLandmarkUseCase@Inject constructor(private val mRepository : SearchLandmarkRepository) {
    suspend fun getLandmarkLocation(apiKey : String, image : String, maxResult : Int) : ApiResult<SearchLandmarkEntity> {
        return mRepository.searchLandmark(apiKey, image, maxResult).first()
    }
}