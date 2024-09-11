package com.project.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.data.api.SearchImageApi
import com.project.data.mapper.ImageListMapper
import com.project.domain.entity.ImageEntity
import com.project.domain.repository.api.SearchImageRepository
import javax.inject.Inject

class SearchImagePagingSource @Inject constructor(
    private val mApiService : SearchImageApi,
    private val mApiKey : String,
    private val mSearchEngine : String,
    private val mKeyword : String
) : PagingSource<Int, ImageEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ImageEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(10)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(10)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageEntity> {
        val page = params.key ?: 1
        return try {
            val response = mApiService.searchImage(mApiKey, mSearchEngine, mKeyword, 10, page)
            val entity = response.items?.map{ ImageListMapper.getImageEntity(it) }?: arrayListOf()
            LoadResult.Page(
                data = entity,
                prevKey = if (page == 1) null else page - 10,
                nextKey = if (entity.isEmpty()) null else page + 10
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}