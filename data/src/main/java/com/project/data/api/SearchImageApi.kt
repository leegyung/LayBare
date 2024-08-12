package com.project.data.api

import com.project.data.model.SearchImageResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchImageApi{
    @GET("customsearch/v1?searchType=image")
    suspend fun searchImage(
        @Query("key") apiKey : String,
        @Query("cx") searchEngine : String,
        @Query("q") keyword : String,
        @Query("num") pageSize : Int,
        @Query("start") pageNum : Int
    ): SearchImageResultDto

    /*
    // 이렇게 여러가지 api를 추가 할수있음
    @GET("/customsearch/v1")
    suspend fun searchImage(
        @Query("key") apiKey : String,
        @Query("cx") searchEngine : String,
        @Query("q") keyword : String,
        @Query("num") pageSize : Int,
        @Query("start") pageNum : Int
    ): Response<SearchImageResultDto>
     */


}