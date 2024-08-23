package com.project.domain.usecase

import android.graphics.Bitmap
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.repository.api.SearchLandmarkRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchLandmarkUseCase@Inject constructor(private val mRepository : SearchLandmarkRepository) {
    operator fun invoke(apiKey : String, image : Bitmap) : Flow<ApiResult<SearchLandmarkEntity>> = flow{
        try{
            emit(ApiResult.ResponseLoading())
            val response = mRepository.searchLandmark(apiKey, image)
            if(response != null){
                emit(ApiResult.ResponseSuccess(response))
            }else{
                emit(ApiResult.ResponseError("위치를 찾을 수 없어요..."))
            }
        }catch (e : HttpException){
            emit(ApiResult.ResponseError(e.localizedMessage?:"알 수 없는 오류가 발생했어요."))
        }catch (e : IOException){
            emit(ApiResult.ResponseError("인터넷 연결을 확인해 주세요."))
        }
    }.flowOn(Dispatchers.IO)
}