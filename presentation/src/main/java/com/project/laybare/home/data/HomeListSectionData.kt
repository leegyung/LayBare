package com.project.laybare.home.data

import com.project.domain.entity.ImageEntity

data class HomeListSectionData(
    val section : String,
    val keyword : String,
    val image : ImageEntity?,
    val imageList : ArrayList<ImageEntity>?
)