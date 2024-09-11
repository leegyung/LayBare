package com.project.laybare.fragment.similarImage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.ImageLabelEntity
import com.project.domain.usecase.SearchImagePagingUseCase
import com.project.laybare.BuildConfig
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimilarImageViewModel @Inject constructor(
    private val mSearchImagePagingUseCase : SearchImagePagingUseCase
) : ViewModel() {
    var mLoadingState = mutableStateOf(false)
        private set
    var mKeywordList = mutableStateListOf<ImageLabelEntity>()
        private set

    var mImageListState: MutableStateFlow<PagingData<ImageEntity>> = MutableStateFlow(value = PagingData.empty())
        private set


    private var mNetworkingJob : Job? = null

    init {
        mKeywordList.addAll(ImageDetailData.getImageLabelList())
        searchImage()
    }



    private fun searchImage() {
        mNetworkingJob?.cancel()

        mNetworkingJob = viewModelScope.launch {
            val keyword = mKeywordList.filter { it.isSelected }.joinToString(separator = ", ") { it.label }
            if(keyword.isEmpty()){
                return@launch
            }

            mImageListState.value = PagingData.empty()

            mSearchImagePagingUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, keyword)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .collect{ result ->
                    mImageListState.value = result
                }
        }


    }

    fun onKeywordClicked(index : Int) {
        mKeywordList.getOrNull(index)?.let{
            mKeywordList[index] = it.copy(isSelected = !it.isSelected)
            searchImage()
        }
    }

    fun onImageClicked(index : Int) : Boolean {
        /*
        _imagesState.value.getOrNull(index)?.let{ image ->
            _imagesState

            ImageDetailData.setNewImageData(if(image.linkError) image.thumbnailLink else image.link)
            return true
        }

         */
        return false
    }



}