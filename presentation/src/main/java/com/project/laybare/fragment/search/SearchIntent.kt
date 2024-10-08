package com.project.laybare.fragment.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.project.domain.entity.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed class SearchEvent {
    data object OnBackClicked : SearchEvent()
    data class OnImageClicked(val url : String) : SearchEvent()
    data class OnKeywordChanged(val keyword : String) : SearchEvent()
    data object OnSearchClicked : SearchEvent()
}

sealed class SearchSideEffect {
    data object NavigateToImageDetail : SearchSideEffect()
    data object PopBackstack : SearchSideEffect()
    data class ShowToastMessage(val msg : String) : SearchSideEffect()
    data class ShowDialog(
        val message : String,
        val option : String,
        val moveToPrevious : Boolean
    ) : SearchSideEffect()
}


@Immutable
data class SearchState(
    val keyword: String = "",
    val isLoading : Boolean = false,
    val imageList : Flow<PagingData<ImageEntity>> = flowOf(value = PagingData.empty())
)