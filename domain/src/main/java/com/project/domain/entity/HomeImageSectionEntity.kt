package com.project.domain.entity

data class HomeImageSectionEntity (
    val sectionType : String,
    val keyword : String,
    val imageList : List<ImageEntity>
)