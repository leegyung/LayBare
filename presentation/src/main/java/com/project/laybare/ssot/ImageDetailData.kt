package com.project.laybare.ssot

import com.project.domain.entity.SearchLandmarkEntity

object ImageDetailData {
    private var mImageUrl = ""
    private var mExtractedText = ""
    private var mLocationData: SearchLandmarkEntity? = null
    private var mContactData : HashMap<String, ArrayList<String>>? = null


    fun setNewImageData(url : String){
        mImageUrl = url
        mExtractedText = ""
        mLocationData = null
        mContactData = null
    }

    fun setExtractedText(text : String) {
        mExtractedText = text
    }

    fun setLocationData(data : SearchLandmarkEntity) {
        mLocationData = data
    }

    fun setContactData(data : HashMap<String, ArrayList<String>>) {
        mContactData = data
    }

    fun getImageUrl() : String = mImageUrl
    fun getExtractedText() : String = mExtractedText
    fun getLocationData() : SearchLandmarkEntity? = mLocationData
    fun getContactList() : HashMap<String, ArrayList<String>>? = mContactData
}