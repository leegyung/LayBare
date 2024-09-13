package com.project.domain.usecase

import com.project.domain.repository.library.TextRecognitionRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExtractTextUseCase @Inject constructor(private val mRepository: TextRecognitionRepository) {
    operator fun invoke(image : ByteArray) : Flow<ApiResult<String>> = flow {
        try {
            emit(ApiResult.ResponseLoading())

            val result = mRepository.extractText(image)
            if(result != null){
                emit(ApiResult.ResponseSuccess(result))
            }else{
                emit(ApiResult.ResponseError("텍스트 추출 오류"))
            }
        }catch (e : Exception) {
            emit(ApiResult.ResponseError(e.localizedMessage?:"텍스트 추출 오류"))
        }
    }.flowOn(Dispatchers.IO)
}