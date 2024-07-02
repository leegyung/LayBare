package com.project.laybare.home

import android.graphics.Bitmap
import android.widget.ImageView
import com.project.domain.entity.ImageEntity

interface HomeListInterface {
    fun onImageClicked(image : ImageEntity, imageView : ImageView)

}