package com.project.domain.repository.library

import android.graphics.Bitmap
import com.project.domain.entity.ImageLabelEntity

interface ImageRecognitionRepository {
    suspend fun getImageLabelList(image : Bitmap) : List<ImageLabelEntity>?
}