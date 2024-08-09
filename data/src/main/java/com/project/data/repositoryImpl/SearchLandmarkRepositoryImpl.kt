package com.project.data.repositoryImpl

import com.project.data.api.SearchLandmarkApi
import com.project.data.mapper.LandmarkDataMapper
import com.project.data.model.RequestFeatureData
import com.project.data.model.RequestImageData
import com.project.data.model.SearchLandmarkRequestBody
import com.project.data.model.SearchLandmarkRequestData
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.repository.SearchLandmarkRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchLandmarkRepositoryImpl @Inject constructor(private val mApiService : SearchLandmarkApi) : SearchLandmarkRepository {
    override suspend fun searchLandmark(apiKey: String, image: String, maxResult: Int) : Flow<ApiResult<SearchLandmarkEntity>> = flow {
        try{
            val data = SearchLandmarkRequestData(
                RequestImageData(image),
                arrayListOf(RequestFeatureData(maxResult))
            )

            val body = SearchLandmarkRequestBody(
                arrayListOf(data)
            )

            val response = mApiService.searchLandmark(apiKey, body)
            if(response.isSuccessful){
                val entity = LandmarkDataMapper.getLandmarkEntity(response.body())
                if(entity != null){
                    emit(ApiResult.ResponseSuccess(entity))
                }else{
                    emit(ApiResult.ResponseError("위치를 찾을 수 없어요..."))
                }
            }else{
                emit(ApiResult.ResponseError("위치를 찾기 애러"))
            }
        }catch (e : Exception) {
            emit(ApiResult.ResponseError("데이터 로딩 실패"))
        }
    }.flowOn(Dispatchers.IO)
}