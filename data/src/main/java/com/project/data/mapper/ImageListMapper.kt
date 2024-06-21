package com.project.data.mapper

import com.project.data.model.ItemDto
import com.project.data.model.SearchImageResultDto
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity

object ImageListMapper {
    fun getImageListEntity(dto : SearchImageResultDto?, keyword : String) : SearchImageResultEntity {
        val total = dto?.queries?.request?.getOrNull(0)?.totalResults?:0
        val nextPageIndex = dto?.queries?.nextPage?.getOrNull(0)?.startIndex?:0
        val correctionQuery = dto?.spelling?.correctedQuery?:""

        val images = arrayListOf<ImageEntity>()
        dto?.items?.forEach {
            images.add(getImageEntity(it))
        }

        return SearchImageResultEntity(
            keyword,
            total,
            nextPageIndex,
            correctionQuery,
            images
        )
    }

    private fun getImageEntity(dto : ItemDto) : ImageEntity {
        val info = dto.image
        return ImageEntity(
            dto.title?:"",
            dto.link?:"",
            info?.contextLink?:"",
            info?.height?:0,
            info?.width?:0,
            info?.thumbnailLink?:""
        )
    }
}