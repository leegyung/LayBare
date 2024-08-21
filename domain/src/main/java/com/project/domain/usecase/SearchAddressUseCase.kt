package com.project.domain.usecase

import com.project.domain.entity.SearchAddressEntity
import com.project.domain.repository.SearchAddressRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(private val mRepository: SearchAddressRepository) {
    operator fun invoke(key : String, geocode : String) : Flow<ApiResult<SearchAddressEntity>> = flow {
        try{
            emit(ApiResult.ResponseLoading())
            val response = mRepository.getAddressData(key, geocode)
            emit(ApiResult.ResponseSuccess(response))
        }catch (e : HttpException){
            emit(ApiResult.ResponseError(e.localizedMessage?:"알 수 없는 오류가 발생했어요."))
        }catch (e : IOException){
            emit(ApiResult.ResponseError("인터넷 연결을 확인해 주세요."))
        }
    }.flowOn(Dispatchers.IO)

}