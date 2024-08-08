package com.project.laybare.Location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.SearchAddressEntity
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.usecase.SearchAddressUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(private val mAddressUseCase: SearchAddressUseCase) : ViewModel() {
    private var mLocationData : SearchLandmarkEntity? = null
    private var mAddressData : SearchAddressEntity? = null

    private val _setAddressText = MutableStateFlow("")
    val mSetAddressText get() = _setAddressText

    fun dataInitializeRequire() : Boolean {
        return mLocationData == null && mAddressData == null
    }

    fun setLocationData(data : SearchLandmarkEntity) {
        mLocationData = data
    }

    fun getLocationData() : SearchLandmarkEntity? {
        return mLocationData
    }

    fun getAddress() {
        if(mLocationData == null){
            return
        }

        viewModelScope.launch {
            val geocode = "${mLocationData!!.latitude},${mLocationData!!.longitude}"

            mAddressUseCase.getAddress(BuildConfig.API_KEY, geocode).collectLatest { result ->
                if(result is ApiResult.ResponseSuccess){
                    mAddressData = result.data
                    _setAddressText.emit(mAddressData?.fullAddress?:"")
                }else{

                }
            }
        }
    }

}