package com.project.domain.usecase

import androidx.paging.PagingData
import com.project.domain.entity.ImageEntity
import com.project.domain.repository.api.SearchImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchImagePagingUseCase @Inject constructor(private val mRepository : SearchImageRepository) {
    suspend operator fun invoke(
        apiKey: String,
        searchEngine : String,
        keyword : String,
        pageSize : Int
    ) : Flow<PagingData<ImageEntity>> {
        return mRepository.getSearchImagePagingSource(apiKey, searchEngine, keyword, pageSize)
    }
}