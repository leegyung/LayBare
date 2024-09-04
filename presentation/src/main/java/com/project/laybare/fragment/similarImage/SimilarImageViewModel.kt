package com.project.laybare.fragment.similarImage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.ImageLabelEntity
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SimilarImageViewModel @Inject constructor(private val mSearchImageUseCase: SearchImageUseCase) : ViewModel() {
    var mLoadingState = mutableStateOf(false)
        private set
    var mKeywordList = mutableStateListOf<ImageLabelEntity>()
        private set
    var mImageList = mutableStateListOf<ImageEntity>()
        private set


    private var mNetworkingJob : Job? = null

    init {
        mKeywordList.addAll(ImageDetailData.getImageLabelList())
        //searchImage()
    }

    private fun searchImage() {

        val keyword = mKeywordList.filter { it.isSelected }.joinToString(separator = ", ") { it.label }
        if(keyword.isEmpty()){
            return
        }

        mImageList.clear()
        mNetworkingJob?.cancel()

        mNetworkingJob = mSearchImageUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, keyword, 1, 10).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {
                    mLoadingState.value = true
                }
                is ApiResult.ResponseSuccess -> {
                    mImageList.addAll(result.data.imageList)
                    mLoadingState.value = false
                }
                is ApiResult.ResponseError -> {
                    mLoadingState.value = false
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onKeywordClicked(index : Int) {
        mKeywordList.getOrNull(index)?.let{
            mKeywordList[index] = it.copy(isSelected = !it.isSelected)
            searchImage()
        }
    }

    fun onImageClicked(index : Int) : Boolean {
        mImageList.getOrNull(index)?.let{ image ->
            ImageDetailData.setNewImageData(if(image.linkError) image.thumbnailLink else image.link)
            return true
        }
        return false
    }



}