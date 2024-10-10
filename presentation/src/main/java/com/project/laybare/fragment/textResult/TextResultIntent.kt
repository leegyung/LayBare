package com.project.laybare.fragment.textResult

import androidx.compose.runtime.Immutable

sealed class TextResultSideEffect {
    data object MoveToPreviousPage : TextResultSideEffect()
}

sealed class TextResultEvent {
    data object MoveToPreviousPage : TextResultEvent()
    data object RollBackText : TextResultEvent()
}

@Immutable
data class TextResultState(
    val originalText : String,
    val modifiedText : String = originalText
)