package com.project.laybare.search

import android.widget.ImageView

interface SearchAdapterInterface {
    fun onImageClicked(view : ImageView, url : String, thumbnail : String)
}