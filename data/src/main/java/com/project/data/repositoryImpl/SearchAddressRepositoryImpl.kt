package com.project.data.repositoryImpl

import com.project.data.api.SearchAddressApi
import com.project.data.mapper.AddressDataMapper
import com.project.domain.entity.SearchAddressEntity
import com.project.domain.repository.SearchAddressRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchAddressRepositoryImpl @Inject constructor(private val mApiService : SearchAddressApi) : SearchAddressRepository {
    override suspend fun getAddressData(key: String, geocode: String): Flow<ApiResult<SearchAddressEntity>> = flow {
        try{
            val response = mApiService.getAddressData(geocode, key)
            if(response.isSuccessful){
                val entity = AddressDataMapper.getAddressEntity(response.body())
                emit(ApiResult.ResponseSuccess(entity))
            }else{
                emit(ApiResult.ResponseError("데이터 로딩 실패"))
            }
        }catch (_:Exception){
            emit(ApiResult.ResponseError("데이터 로딩 실패"))
        }
    }.flowOn(Dispatchers.IO)

}