package com.project.data.repositoryImpl.api

import com.project.data.api.SearchImageApi
import com.project.data.mapper.ImageListMapper
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.repository.api.SearchImageRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchImageRepositoryImpl @Inject constructor(private val mApiService : SearchImageApi) :
    SearchImageRepository {
    override suspend fun getImageList(apiKey : String, searchEngine : String, keyword: String, page: Int, size: Int): SearchImageResultEntity {
        val response = mApiService.searchImage(apiKey, searchEngine, keyword, size, page)
        val entity = ImageListMapper.getImageListEntity(response, keyword)
        return entity
    }

}