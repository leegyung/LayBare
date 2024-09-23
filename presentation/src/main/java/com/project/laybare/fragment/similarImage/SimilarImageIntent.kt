package com.project.laybare.fragment.similarImage

import androidx.paging.PagingData
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.ImageLabelEntity
import kotlinx.coroutines.flow.MutableStateFlow


sealed class SimilarImageEvent {
    data object OnBackClicked : SimilarImageEvent()
    data class OnKeywordClicked(val index: Int) : SimilarImageEvent()
    data class OnImageClicked(val image : ImageEntity) : SimilarImageEvent()
    data class OnErrorOccurred(val message: String) : SimilarImageEvent()
}

sealed class SimilarImageSideEffect {
    data class Navigate(val destination : String) : SimilarImageSideEffect()
    data class ShowToast(val message : String) : SimilarImageSideEffect()
}


data class SimilarImageState(
    val isLoading : Boolean = false,
    val keyword : List<ImageLabelEntity> = emptyList(),
    val imageList : MutableStateFlow<PagingData<ImageEntity>> = MutableStateFlow(value = PagingData.empty())
)









