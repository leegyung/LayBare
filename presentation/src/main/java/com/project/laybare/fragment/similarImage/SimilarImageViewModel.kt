package com.project.laybare.fragment.similarImage

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.ImageEntity
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SimilarImageViewModel @Inject constructor(private val mSearchImageUseCase: SearchImageUseCase) : ViewModel() {
    private val _keywordList = mutableStateListOf<String>()
    private val _imageList = mutableStateListOf<ImageEntity>()


    val mKeywordList get() = _keywordList
    val mImageList get() = _imageList


    init {
        _keywordList.addAll(ImageDetailData.getImageLabelList())
        searchImage()
    }

    private fun searchImage() {

        val keyword = _keywordList.joinToString(separator = ", ")

        mSearchImageUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, keyword, 1, 10).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {

                }
                is ApiResult.ResponseSuccess -> {
                    _imageList.addAll(result.data.imageList)
                }
                is ApiResult.ResponseError -> {

                }
            }
        }.launchIn(viewModelScope)
    }



}