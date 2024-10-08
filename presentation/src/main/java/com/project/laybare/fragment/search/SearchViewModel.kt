package com.project.laybare.fragment.search

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.usecase.SearchImagePagingUseCase
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import com.project.laybare.fragment.similarImage.SimilarImageSideEffect
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mSearchImagePagingUseCase : SearchImagePagingUseCase,
) : ContainerHost<SearchState, SearchSideEffect>, ViewModel() {

    override val container: Container<SearchState, SearchSideEffect> = container(SearchState())

    fun handleEvent(event : SearchEvent) {
        when(event) {
            is SearchEvent.OnBackClicked -> moveToPreviousPage()
            is SearchEvent.OnImageClicked -> moveToImageDetail(event.url)
            is SearchEvent.OnKeywordChanged -> keywordChanged(event.keyword)
            is SearchEvent.OnSearchClicked -> changePagingSource()
        }
    }

    private fun moveToPreviousPage() = intent {
        postSideEffect(SearchSideEffect.PopBackstack)
    }

    private fun moveToImageDetail(url : String) = intent {
        ImageDetailData.setNewImageData(url)
        postSideEffect(SearchSideEffect.NavigateToImageDetail)
    }

    private fun keywordChanged(newKeyword : String) = intent {
        reduce { state.copy(keyword = newKeyword) }
    }

    private fun changePagingSource() = intent {
        if(state.keyword.isEmpty()) {
            postSideEffect(SearchSideEffect.ShowToastMessage("검색 키워드를 입력해 주세요."))
        }else {
            val imagePagingFlow = mSearchImagePagingUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, state.keyword, 10)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .catch { error ->
                    postSideEffect(SearchSideEffect.ShowDialog(error.localizedMessage?:"알 수 없는 오류가 발생했어요", "돌아가기", true))
                }
            reduce { state.copy(imageList = imagePagingFlow) }
        }
    }




}