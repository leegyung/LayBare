package com.project.domain.entity

data class SearchImageResultEntity(
    val keyWord : String,
    val totalResults : Long,
    val nextPageIndex : Int,
    val correctQuery : String,
    val imageList : ArrayList<ImageEntity>
)

data class ImageEntity(
    val title : String,
    val link : String,
    val contextLink : String,
    val height : Int,
    val width : Int,
    val thumbnailLink : String,
    var linkError : Boolean = false
)
