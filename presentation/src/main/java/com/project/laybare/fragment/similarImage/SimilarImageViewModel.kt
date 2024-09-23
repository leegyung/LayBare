package com.project.laybare.fragment.similarImage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.project.domain.entity.ImageEntity
import com.project.domain.usecase.SearchImagePagingUseCase
import com.project.laybare.BuildConfig
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimilarImageViewModel @Inject constructor(
    private val mSearchImagePagingUseCase : SearchImagePagingUseCase
) : ViewModel() {
    private var mNetworkingJob : Job? = null

    private val _uiState = MutableStateFlow(SimilarImageState())
    val mUiState get() = _uiState

    private val _uiSideEffect = MutableSharedFlow<SimilarImageSideEffect>()
    val mUiSideEffect get() = _uiSideEffect.asSharedFlow()



    init {
        initializeData()
        searchImage()
    }

    private fun initializeData(){
        val keywords = ImageDetailData.getImageLabelList()
        _uiState.update { it.copy( keyword = keywords ) }
    }


    fun processEvent(event : SimilarImageEvent) {
        when(event){
            is SimilarImageEvent.OnBackClicked -> {}
            is SimilarImageEvent.OnImageClicked -> onImageClicked(event.image)
            is SimilarImageEvent.OnKeywordClicked -> onKeywordClicked(event.index)
            is SimilarImageEvent.OnErrorOccurred -> {
                viewModelScope.launch {
                    _uiSideEffect.emit(SimilarImageSideEffect.ShowToast(event.message))
                }
            }
        }
    }


    private fun searchImage() {
        mNetworkingJob?.cancel()


        mNetworkingJob = viewModelScope.launch {
            val keyword = _uiState.value.keyword.filter { it.isSelected }.joinToString(separator = ", ") { it.label }
            if(keyword.isEmpty()){
                return@launch
            }


            mSearchImagePagingUseCase(BuildConfig.API_KEY + "asdasdasd", BuildConfig.SEARCH_ENGINE, keyword, 10)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .collect{ result ->
                    _uiState.update {
                        it.copy(isLoading = false).apply {
                            imageList.update { result }
                        }
                    }
                }
        }


    }

    private fun onKeywordClicked(targetIndex : Int) {

        val newKeywordData = _uiState.value.keyword.mapIndexed { index, image ->
            if(index == targetIndex){
                image.copy(isSelected = !image.isSelected)
            }else{
                image
            }
        }

        _uiState.update { it.copy(keyword = newKeywordData) }
        searchImage()
    }

    private fun onImageClicked(image : ImageEntity) {
        viewModelScope.launch {
            ImageDetailData.setNewImageData(if(image.linkError) image.thumbnailLink else image.link)
            _uiSideEffect.emit(SimilarImageSideEffect.Navigate("ImageDetail"))
        }
    }




}