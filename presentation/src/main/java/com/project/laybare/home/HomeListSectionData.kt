package com.project.laybare.home

import com.project.domain.entity.ImageEntity

data class HomeListSectionData(
    val section : String,
    val image : ImageEntity?,
    val imageList : ArrayList<ImageEntity>?
)