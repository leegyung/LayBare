package com.project.laybare.home

import com.project.domain.entity.ImageEntity

interface HomeListInterface {
    fun onImageClicked(image : ImageEntity)
}