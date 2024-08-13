package com.project.laybare.fragment.location

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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(
    private val mAddressUseCase: SearchAddressUseCase,
    private val mPictureUseCase: SearchImageUseCase
) : ViewModel() {

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

        val geocode = "${mLocationData!!.latitude},${mLocationData!!.longitude}"

        mAddressUseCase(BuildConfig.API_KEY, geocode).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {

                }
                is ApiResult.ResponseSuccess -> {
                    mAddressData = result.data
                    _setAddressText.emit(mAddressData?.fullAddress?:"")
                    getImageList()
                }
                is ApiResult.ResponseError -> {
                    _apiError.emit(result.errorMessage?:"주소 검색 애러")
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getImageList() {
        val locationName = getLocationName()

        if(locationName.isEmpty()){
            return
        }

        mPictureUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, locationName, 1, 10).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {

                }
                is ApiResult.ResponseSuccess -> {
                    result.data?.imageList?.let{
                        mImageList.addAll(it)
                        mImageAdapter.notifyItemRangeInserted(0, it.size - 1)
                    }
                }
                is ApiResult.ResponseError -> {
                    _apiError.emit(result.errorMessage?:"사진 검색 애러")
                }
            }
        }.launchIn(viewModelScope)
    }

}