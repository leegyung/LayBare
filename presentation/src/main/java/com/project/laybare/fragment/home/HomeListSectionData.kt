package com.project.laybare.fragment.home

import com.project.domain.entity.ImageEntity

data class HomeListSectionData(
    val section : String,
    val keyword : String,
    val image : ImageEntity?,
    val imageList : ArrayList<ImageEntity>?
)