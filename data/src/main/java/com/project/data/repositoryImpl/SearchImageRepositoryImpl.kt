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
import javax.inject.Inject

class SearchImageRepositoryImpl @Inject constructor(private val mApiService : SearchImageApi) : SearchImageRepository {

    override suspend fun getImageList(apiKey : String, searchEngine : String, keyword: String, page: Int, size: Int): Flow<ApiResult<SearchImageResultEntity>> = flow {
        try{
            val response = mApiService.searchImage(apiKey, searchEngine, keyword, size, page)
            if(response.isSuccessful){
                val body = response.body()
                if(body != null){
                    val entity = ImageListMapper.getImageListEntity(body, keyword)
                    emit(ApiResult.ResponseSuccess(entity))
                }else{
                    emit(ApiResult.ResponseError("데이터 로딩 실패"))
                }
            }else{
                emit(ApiResult.ResponseError("데이터 로딩 실패"))
            }
        }catch (e:Exception){
            emit(ApiResult.ResponseError("데이터 로딩 실패"))
        }
    }.flowOn(Dispatchers.IO)

}