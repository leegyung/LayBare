package com.project.domain.usecase

import android.graphics.Bitmap
import com.project.domain.repository.library.ImageRecognitionRepository
import com.project.domain.util.LibraryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExtractImageLabelUseCase @Inject constructor(private val mRepository : ImageRecognitionRepository) {
    operator fun invoke(bitmap: Bitmap) : Flow<LibraryResult<List<String>>> = flow {
        try{
            emit(LibraryResult.ResponseLoading())

            val result = mRepository.getImageLabelList(bitmap)
            if(result.isNullOrEmpty()){
                emit(LibraryResult.ResponseError("이미지에서 발견한 라벨이 없어요..."))
            }else{
                emit(LibraryResult.ResponseSuccess(result))
            }
        }catch (e : Exception) {
            emit(LibraryResult.ResponseError("이미지 라벨링 실패"))
        }
    }.flowOn(Dispatchers.IO)

}