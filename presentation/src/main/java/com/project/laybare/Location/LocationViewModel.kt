package com.project.laybare.Location

import androidx.lifecycle.ViewModel
import com.project.domain.usecase.SearchLandmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(private val mUseCase: SearchLandmarkUseCase) : ViewModel() {

}