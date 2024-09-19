package com.project.data.repositoryImpl.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.data.api.SearchImageApi
import com.project.data.mapper.ImageListMapper
import com.project.data.pagingSource.SearchImagePagingSource
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.repository.api.SearchImageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchImageRepositoryImpl @Inject constructor(
    private val mApiService : SearchImageApi
) : SearchImageRepository {

    override suspend fun getSearchImagePagingSource(
        apiKey: String,
        searchEngine: String,
        keyword: String,
        pageSize : Int
    ): Flow<PagingData<ImageEntity>> {

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { SearchImagePagingSource(mApiService, apiKey, searchEngine, keyword) }
        ).flow

    }


    override suspend fun searchImage(apiKey : String, searchEngine : String, keyword: String, page: Int, size: Int): SearchImageResultEntity {
        val response = mApiService.searchImage(apiKey, searchEngine, keyword, size, page)
        val entity = ImageListMapper.getImageListEntity(response, keyword)
        return entity
    }



}