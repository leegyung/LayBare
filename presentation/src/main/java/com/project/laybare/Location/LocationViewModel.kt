package com.project.laybare.Location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchAddressEntity
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.usecase.SearchAddressUseCase
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(private val mAddressUseCase: SearchAddressUseCase, private val mPictureUseCase: SearchImageUseCase) : ViewModel() {

    private var mLocationData : SearchLandmarkEntity? = null
    private var mAddressData : SearchAddressEntity? = null

    private val _apiError = MutableSharedFlow<String>()
    private val _setAddressText = MutableStateFlow("")

    val mApiError = _apiError.asSharedFlow()
    val mSetAddressText get() = _setAddressText


    private val mImageList = arrayListOf<ImageEntity>()
    private val mImageAdapter = LocationImageAdapter(mImageList)

    fun dataInitializeRequire() : Boolean {
        return mLocationData == null && mAddressData == null
    }

    fun setLocationData(data : SearchLandmarkEntity) {
        mLocationData = data
    }

    fun getLocationData() : SearchLandmarkEntity? {
        return mLocationData
    }

    fun getLocationName() : String {
        return mLocationData?.description?:""
    }

    fun getImageListAdapter() : LocationImageAdapter {
        return mImageAdapter
    }

    fun getAddress() {
        if(mLocationData == null){
            return
        }

        viewModelScope.launch {
            val geocode = "${mLocationData!!.latitude},${mLocationData!!.longitude}"
            val result = mAddressUseCase.getAddress(BuildConfig.API_KEY, geocode)

            if(result is ApiResult.ResponseSuccess){
                mAddressData = result.data
                _setAddressText.emit(mAddressData?.fullAddress?:"")
            }else{

            }
        }
    }

    fun getImageList() {
        val locationName = getLocationName()
        if(locationName.isEmpty()){
            return
        }

        viewModelScope.launch {
            val result = mPictureUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, locationName, 1, 10)
            if(result is ApiResult.ResponseSuccess){
                result.data?.imageList?.let{
                    mImageList.addAll(it)
                    mImageAdapter.notifyItemRangeInserted(0, it.size - 1)
                }
            }else{
                _apiError.emit(result.errorMessage?:"주소 검색 애러")
            }
        }
    }

}