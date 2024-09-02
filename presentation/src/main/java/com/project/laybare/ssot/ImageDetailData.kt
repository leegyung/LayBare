package com.project.laybare.ssot

import com.project.domain.entity.SearchLandmarkEntity

object ImageDetailData {
    private var mImageUrl = ""
    private var mExtractedText = ""
    private var mLocationData: SearchLandmarkEntity? = null
    private var mContactData : HashMap<String, ArrayList<String>>? = null
    private var mLabelList : List<String>? = null


    fun setNewImageData(url : String){
        mImageUrl = url
        mExtractedText = ""
        mLocationData = null
        mContactData = null
        mLabelList = null
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

    fun setImageLabelList(labels : List<String>) {
        mLabelList = labels
    }

    fun getImageUrl() : String = mImageUrl
    fun getExtractedText() : String = mExtractedText
    fun getLocationData() : SearchLandmarkEntity? = mLocationData
    fun getContactList() : HashMap<String, ArrayList<String>>? = mContactData

    fun getImageLabelList() : List<String> = mLabelList?: arrayListOf()
}