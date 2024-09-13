package com.project.domain.usecase

import com.project.domain.entity.ImageLabelEntity
import com.project.domain.repository.library.ImageRecognitionRepository
import com.project.domain.util.LibraryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import sun.font.GlyphRenderData
import javax.inject.Inject

class ExtractImageLabelUseCase @Inject constructor(private val mRepository : ImageRecognitionRepository) {
    operator fun invoke(image: ByteArray) : Flow<LibraryResult<List<ImageLabelEntity>>> = flow {
        try{
            emit(LibraryResult.ResponseLoading())

            val result = mRepository.getImageLabelList(image)
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