package com.project.data.model


data class SearchImageResultDto(
    val queries : QueryDto?,
    val spelling : SpellingDto?,
    val items : ArrayList<ImageItemDto>?
)
data class QueryDto(
    val request : ArrayList<RequestDto>?,
    val nextPage : ArrayList<NextPageDto>?
)
data class RequestDto(
    val totalResults : Long?,
    val searchTerms : String?,
    val count : Int?,
    val startIndex : Int?
)
data class NextPageDto(
    val totalResults : Long?,
    val searchTerms : String?,
    val count : Int?,
    val startIndex : Int?
)
data class SpellingDto(
    val correctedQuery : String?
)
data class ImageItemDto(
    val title : String?,
    val link : String?,
    val image : ImageInfoDto?
)
data class ImageInfoDto(
    val contextLink : String?,
    val height : Int?,
    val width : Int?,
    val thumbnailLink : String?
)

