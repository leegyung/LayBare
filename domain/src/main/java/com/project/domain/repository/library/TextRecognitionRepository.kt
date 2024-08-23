package com.project.domain.repository.library

import android.graphics.Bitmap

interface TextRecognitionRepository {
    suspend fun extractText(bitmap: Bitmap) : String?

    suspend fun extractTextEntity(text : String) : HashMap<String, ArrayList<String>>?
}