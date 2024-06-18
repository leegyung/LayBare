package com.project.domain.usecase

import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.repository.SearchImageRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchImageUseCase @Inject constructor(private val mRepository : SearchImageRepository) {
    suspend fun getImageList(apiKey : String, searchEngine : String, keyword : String, page : Int, size : Int) : Flow<ApiResult<SearchImageResultEntity>> {
        return mRepository.getImageList(apiKey, searchEngine, keyword, page, size)
    }
}