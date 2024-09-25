package com.project.laybare.fragment.similarImage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.domain.entity.ImageEntity
import com.project.domain.usecase.SearchImagePagingUseCase
import com.project.laybare.BuildConfig
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class SimilarImageViewModel @Inject constructor(
    private val mSearchImagePagingUseCase : SearchImagePagingUseCase
) : ContainerHost<SimilarImageState, SimilarImageSideEffect>, ViewModel() {

    override val container: Container<SimilarImageState, SimilarImageSideEffect> = container(SimilarImageState())
    private val mEventChannel = Channel<SimilarImageEvent>(Channel.UNLIMITED)

    init {
        initializeState()
        processEvent()
        setNewImagePagingSource()
    }

    fun sendEvent(event : SimilarImageEvent) {
        viewModelScope.launch {
            mEventChannel.send(event)
        }
    }

    private fun processEvent() {
        mEventChannel.receiveAsFlow().onEach {
            when(it){
                is SimilarImageEvent.OnBackClicked -> onBackPressed()
                is SimilarImageEvent.OnImageClicked -> navigateToImageDetail(it.image)
                is SimilarImageEvent.OnKeywordClicked -> onKeywordClicked(it.index)
                is SimilarImageEvent.OnPagingError -> handlePagingError(it.message)
                is SimilarImageEvent.OnLoadingStateChanged -> changeLoadingState(it.isLoading)
            }
        }.launchIn(viewModelScope)
    }


    private fun initializeState() = intent {
        val keywords = ImageDetailData.getImageLabelList()
        reduce { state.copy(keyword = keywords, isLoading = true) }
    }



    private fun onBackPressed() = intent {
        postSideEffect(SimilarImageSideEffect.PopBackstack)
    }

    private fun changeLoadingState(isLoading : Boolean) = intent {
        reduce { state.copy(isLoading= isLoading) }
    }

    private fun navigateToImageDetail(image: ImageEntity) = intent {
        ImageDetailData.setNewImageData(if(image.linkError) image.thumbnailLink else image.link)
        postSideEffect(SimilarImageSideEffect.NavigateToImageDetail)
    }

    private fun handlePagingError(error : String?) = intent {
        reduce { state.copy(imageList = flowOf(PagingData.empty())) }
        postSideEffect(SimilarImageSideEffect.ShowDialog(error?:"알 수 없는 오류가 발생했습니다", "돌아가기", true))
    }

    private fun onKeywordClicked(targetIndex : Int) = intent {
        val newKeywordData = state.keyword.mapIndexed { index, label ->
            if(index == targetIndex){
                label.copy(isSelected = !label.isSelected)
            }else{
                label
            }
        }
        reduce { state.copy(keyword = newKeywordData) }

        setNewImagePagingSource()
    }


    private fun setNewImagePagingSource() = intent {
        val keyword = state.keyword.filter { it.isSelected }.joinToString(separator = ", ") { it.label }
        if(keyword.isEmpty()){
            postSideEffect(SimilarImageSideEffect.ShowDialog("선택된 검색 키워드가 없어요", "확인", false))
        }else {
            val imagePagingFlow = mSearchImagePagingUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, keyword, 10)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .catch { error ->
                    postSideEffect(SimilarImageSideEffect.ShowDialog(error.localizedMessage?:"알 수 없는 오류가 발생했어요", "돌아가기", true))
                }

            reduce { state.copy(imageList = imagePagingFlow) }
        }
    }








}