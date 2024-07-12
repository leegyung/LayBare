package com.project.laybare.Location

import androidx.lifecycle.ViewModel
import com.project.domain.entity.LandmarkEntity
import com.project.domain.usecase.SearchLandmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {
    private var mLocationData : LandmarkEntity? = null

    fun setLocationData(data : LandmarkEntity) {
        mLocationData = data
    }

    fun getLocationData() : LandmarkEntity? {
        return mLocationData
    }

}