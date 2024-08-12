package com.project.data.repositoryImpl

import android.graphics.Bitmap
import android.util.Base64
import com.project.data.api.SearchLandmarkApi
import com.project.data.mapper.LandmarkDataMapper
import com.project.data.model.RequestFeatureData
import com.project.data.model.RequestImageData
import com.project.data.model.SearchLandmarkRequestBody
import com.project.data.model.SearchLandmarkRequestData
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.repository.SearchLandmarkRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class SearchLandmarkRepositoryImpl @Inject constructor(private val mApiService : SearchLandmarkApi) : SearchLandmarkRepository {
    override suspend fun searchLandmark(apiKey: String, image: Bitmap) : SearchLandmarkEntity? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

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