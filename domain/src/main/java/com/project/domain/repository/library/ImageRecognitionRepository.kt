package com.project.domain.repository.library

import com.project.domain.entity.ImageLabelEntity

interface ImageRecognitionRepository {
    suspend fun getImageLabelList(image : ByteArray) : List<ImageLabelEntity>?
}