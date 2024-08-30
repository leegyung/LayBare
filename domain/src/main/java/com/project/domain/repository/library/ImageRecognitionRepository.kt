package com.project.domain.repository.library

import android.graphics.Bitmap

interface ImageRecognitionRepository {
    suspend fun getImageLabelList(image : Bitmap) : List<String>?
}