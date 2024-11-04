package com.project.laybare.fragment.home

import androidx.compose.runtime.Immutable
import com.project.domain.entity.HomeImageSectionEntity

sealed class HomeEvent {
    data class MoveToImageDetail(val url : String) : HomeEvent()
    data object MoveToSearchPage : HomeEvent()
}

sealed class HomeSideEffect {
    data class CreateDialog(val msg : String) : HomeSideEffect()
}

@Immutable
data class HomeUiState(
    val isLoading : Boolean = true,
    val imageSections : List<HomeImageSectionEntity> = emptyList()
)