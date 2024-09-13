package com.project.data.repositoryImpl.api

import android.graphics.Bitmap
import android.util.Base64
import com.project.data.api.SearchLandmarkApi
import com.project.data.mapper.LandmarkDataMapper
import com.project.data.model.RequestFeatureData
import com.project.data.model.RequestImageData
import com.project.data.model.SearchLandmarkRequestBody
import com.project.data.model.SearchLandmarkRequestData
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.repository.api.SearchLandmarkRepository
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class SearchLandmarkRepositoryImpl @Inject constructor(private val mApiService : SearchLandmarkApi) :
    SearchLandmarkRepository {
    override suspend fun searchLandmark(apiKey: String, image: ByteArray) : SearchLandmarkEntity? {
        val base64Image = Base64.encodeToString(image, Base64.DEFAULT)


        val data = SearchLandmarkRequestData(
            RequestImageData(base64Image),
            arrayListOf(RequestFeatureData(1))
        )

        val body = SearchLandmarkRequestBody(
            arrayListOf(data)
        )
        val response = mApiService.searchLandmark(apiKey, body)
        val entity = LandmarkDataMapper.getLandmarkEntity(response)

        return entity
    }
}