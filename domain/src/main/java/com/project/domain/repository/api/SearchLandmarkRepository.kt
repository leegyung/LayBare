package com.project.domain.repository.api

import com.project.domain.entity.SearchLandmarkEntity

interface SearchLandmarkRepository {
    suspend fun searchLandmark(apiKey : String, image : ByteArray) : SearchLandmarkEntity?
}