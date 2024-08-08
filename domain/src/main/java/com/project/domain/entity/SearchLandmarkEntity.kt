package com.project.domain.entity


data class SearchLandmarkEntity(
    val description : String?,
    val score : Float,
    val latitude : Float,
    val longitude : Float
)