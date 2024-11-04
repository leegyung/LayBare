package com.project.laybare.fragment.home

import androidx.compose.runtime.Immutable
import com.project.domain.entity.HomeImageSectionEntity

sealed class HomeEvent {
    data class MoveToImageDetail(val url : String) : HomeEvent()
    data object SearchBtnClicked : HomeEvent()
    data object SelectImageBtnClicked : HomeEvent()
    data object SelectImageFromAlbumClicked : HomeEvent()
    data object TakePictureByCameraClicked : HomeEvent()

}

sealed class HomeSideEffect {
    data class CreateDialog(val msg : String) : HomeSideEffect()
    data object NavigateToSearchPage : HomeSideEffect()
    data object NavigateToImageDetailPage : HomeSideEffect()

    data object CreateImageSelectDialog : HomeSideEffect()
    data object OpenAlbum : HomeSideEffect()
    data object LaunchCamera : HomeSideEffect()

}

@Immutable
data class HomeUiState(
    val isLoading : Boolean = true,
    val imageSections : List<HomeImageSectionEntity> = emptyList()
)