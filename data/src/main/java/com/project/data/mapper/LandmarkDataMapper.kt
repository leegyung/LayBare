package com.project.data.mapper

import com.project.data.model.SearchLandmarkResultDto
import com.project.domain.entity.SearchLandmarkEntity


fun SearchLandmarkResultDto.toSearchLandMark() : SearchLandmarkEntity? {
    val data = responses?.getOrNull(0)?.landmarkAnnotations?.getOrNull(0)?: return null
    val location = data.locations?.getOrNull(0)?.latLng


    val entity = SearchLandmarkEntity(
        data.description?:"",
        data.score?:0f,
        location?.latitude?:0f,
        location?.longitude?:0f
    )

    return entity
}
