package com.project.data.model

data class SearchLandmarkRequestBody(
    val requests : ArrayList<SearchLandmarkRequestData>
)

data class SearchLandmarkRequestData(
    val image : RequestImageData,
    val features : ArrayList<RequestFeatureData>
)

data class RequestImageData(
    val content : String
)

data class RequestFeatureData(
    val maxResults : Int,
    val type : String = "LANDMARK_DETECTION"
)



data class SearchLandmarkResultDto(
    val responses : ArrayList<SearchLandmarkDto>?
)

data class SearchLandmarkDto(
    val landmarkAnnotations : ArrayList<LandmarkDto>?
)

data class LandmarkDto(
    val mid : String?,
    val description : String?,
    val score : Float?,
    val locations : ArrayList<LocationDto>?
)

data class LocationDto(
    val latLng : LongitudeLatitudeDto?
)

data class LongitudeLatitudeDto(
    val latitude : Float?,
    val longitude : Float?
)

