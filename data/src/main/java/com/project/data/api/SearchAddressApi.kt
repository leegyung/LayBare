package com.project.data.api

import com.project.data.model.SearchAddressDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAddressApi {
    @GET("maps/api/geocode/json")
    suspend fun getAddressData(
        @Query("latlng") latlng : String,
        @Query("key") apiKey : String,
        @Query("language") language : String = "ko"
    ): Response<SearchAddressDto>
}