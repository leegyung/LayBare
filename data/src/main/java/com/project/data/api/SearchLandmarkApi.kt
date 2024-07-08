package com.project.data.api

import com.project.data.model.SearchLandmarkRequestBody
import com.project.data.model.SearchLandmarkResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchLandmarkApi {
    @GET("v1/images:annotate")
    suspend fun searchLandmark(
        @Query("key") key : String,
        @Body body : SearchLandmarkRequestBody
    ): Response<SearchLandmarkResultDto>
}