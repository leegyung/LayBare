package com.project.data.repositoryImpl

import com.project.data.api.SearchImageApi
import com.project.data.mapper.ImageListMapper
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.repository.SearchImageRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchImageRepositoryImpl @Inject constructor(private val mApiService : SearchImageApi) : SearchImageRepository {
    override suspend fun getImageList(apiKey : String, searchEngine : String, keyword: String, page: Int, size: Int): SearchImageResultEntity {
        val response = mApiService.searchImage(apiKey, searchEngine, keyword, size, page)
        val entity = ImageListMapper.getImageListEntity(response, keyword)
        return entity
    }

}