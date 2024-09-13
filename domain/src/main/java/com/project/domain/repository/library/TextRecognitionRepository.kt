package com.project.domain.repository.library




interface TextRecognitionRepository {
    suspend fun extractText(image: ByteArray) : String?

    suspend fun extractTextEntity(text : String) : HashMap<String, ArrayList<String>>?
}