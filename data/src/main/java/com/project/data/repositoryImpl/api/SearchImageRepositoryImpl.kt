package com.project.data.repositoryImpl.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.data.api.SearchImageApi
import com.project.data.mapper.toHomeImageSection
import com.project.data.mapper.toSearchImageResult
import com.project.data.pagingSource.SearchImagePagingSource
import com.project.data.util.RandomWordGenerator
import com.project.domain.entity.HomeImageSectionEntity
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.repository.api.SearchImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
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
        val entity = response.toSearchImageResult(keyword)
        return entity
    }


    override suspend fun searchHomeSectionImage(apiKey : String, searchEngine : String, sectionSize : Int, bannerImageNum : Int, normalImageNum : Int): List<HomeImageSectionEntity> = coroutineScope {
        val randomKeywords = RandomWordGenerator().getRandomWord(sectionSize)

        List(sectionSize) { index ->
            val isBanner = index == 0
            val keyword = randomKeywords[index]


            val images = mApiService.searchImage(
                apiKey,
                searchEngine,
                keyword,
                if(isBanner) bannerImageNum else normalImageNum,
                1
            )

            images.toHomeImageSection(
                keyword,
                if(isBanner) "Banner" else "Normal"
            )
        }
    }



}