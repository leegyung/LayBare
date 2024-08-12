package com.project.domain.repository

import android.graphics.Bitmap
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface SearchLandmarkRepository {
    suspend fun searchLandmark(apiKey : String, image : Bitmap) : SearchLandmarkEntity?
}