package com.project.data.mapper

import com.project.data.model.ImageItemDto
import com.project.data.model.SearchImageResultDto
import com.project.domain.entity.HomeImageSectionEntity
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity



fun SearchImageResultDto.toSearchImageResult(keyword : String) : SearchImageResultEntity {
    val total = queries?.request?.getOrNull(0)?.totalResults?:0
    val nextPageIndex = queries?.nextPage?.getOrNull(0)?.startIndex?:0
    val correctionQuery = spelling?.correctedQuery?:""
    val images = arrayListOf<ImageEntity>()


    items?.forEach {
        images.add(it.toImageData())
    }

    return SearchImageResultEntity(
        keyword,
        total,
        nextPageIndex,
        correctionQuery,
        images
    )
}


fun SearchImageResultDto.toHomeImageSection(keyword: String, section : String) : HomeImageSectionEntity {
    val images = items?.map { it.toImageData() }



    return HomeImageSectionEntity(
        sectionType = section,
        keyword = keyword,
        imageList = images?: emptyList()
    )
}



fun ImageItemDto.toImageData() : ImageEntity {
    return ImageEntity(
        title?:"",
        link?:"",
        image?.contextLink?:"",
        image?.height?:0,
        image?.width?:0,
        image?.thumbnailLink?:""
    )
}




