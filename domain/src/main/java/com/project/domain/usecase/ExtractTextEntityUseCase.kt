package com.project.domain.usecase

import com.project.domain.repository.library.TextRecognitionRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExtractTextEntityUseCase @Inject constructor(private val mRepository: TextRecognitionRepository) {
    operator fun invoke(text : String) :Flow<ApiResult<HashMap<String, ArrayList<String>>>> = flow {
        try{
            emit(ApiResult.ResponseLoading())

            val result = mRepository.extractTextEntity(text)
            if(result.isNullOrEmpty()){
                emit(ApiResult.ResponseError("검색된 연락처가 없어요..."))
            }else{
                emit(ApiResult.ResponseSuccess(result))
            }
        }catch (_ : Exception){
            emit(ApiResult.ResponseError("연락처 검색 오류"))
        }
    }.flowOn(Dispatchers.IO)
}