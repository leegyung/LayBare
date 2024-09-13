package com.project.domain.usecase

import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.repository.api.SearchImageRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class SearchImageUseCase @Inject constructor(private val mRepository : SearchImageRepository) {

    operator fun invoke(apiKey : String, searchEngine : String, keyword : String, page : Int, size : Int) : Flow<ApiResult<SearchImageResultEntity>> = flow {
        try{
            emit(ApiResult.ResponseLoading())
            val response = mRepository.searchImage(apiKey, searchEngine, keyword, page, size)
            emit(ApiResult.ResponseSuccess(response))
        }catch (e : IOException){
            emit(ApiResult.ResponseError("인터넷 연결을 확인해 주세요."))
        }catch (e : Exception){
            emit(ApiResult.ResponseError(e.localizedMessage?:"알 수 없는 오류가 발생했어요."))
        }
    }.flowOn(Dispatchers.IO)

}