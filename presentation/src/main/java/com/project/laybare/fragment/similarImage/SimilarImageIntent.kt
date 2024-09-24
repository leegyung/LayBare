package com.project.laybare.fragment.similarImage

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.ImageLabelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


sealed class SimilarImageEvent {
    data object OnBackClicked : SimilarImageEvent()
    data class OnKeywordClicked(val index: Int) : SimilarImageEvent()
    data class OnImageClicked(val image : ImageEntity) : SimilarImageEvent()
    data class OnPagingError(val message: String) : SimilarImageEvent()
    data class OnLoadingStateChanged(val isLoading : Boolean) : SimilarImageEvent()
}

sealed class SimilarImageSideEffect {
    data object NavigateToImageDetail : SimilarImageSideEffect()
    data object PopBackstack : SimilarImageSideEffect()
    data class ShowDialog(
        val message : String,
        val option : String,
        val moveToPrevious : Boolean
    ) : SimilarImageSideEffect()
}

@Immutable
data class SimilarImageState(
    val isLoading : Boolean = false,
    val keyword : List<ImageLabelEntity> = emptyList(),
    val imageList : Flow<PagingData<ImageEntity>> = flowOf(value = PagingData.empty())
)









