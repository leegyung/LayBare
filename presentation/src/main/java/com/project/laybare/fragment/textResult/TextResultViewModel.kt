package com.project.laybare.fragment.textResult

import androidx.lifecycle.ViewModel
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TextResultViewModel @Inject constructor() : ContainerHost<TextResultState, TextResultSideEffect>, ViewModel() {


    override val container: Container<TextResultState, TextResultSideEffect> = container(
        TextResultState(ImageDetailData.getExtractedText())
    )

    fun handelEvent(event: TextResultEvent) {
        when(event){
            is TextResultEvent.RollBackText -> rollbackText()
            is TextResultEvent.MoveToPreviousPage -> moveToPreviousPage()
            is TextResultEvent.OnTextChanged -> onTextChanged(event.text)
        }
    }


    private fun moveToPreviousPage() = intent {
        postSideEffect(TextResultSideEffect.MoveToPreviousPage)
    }

    private fun rollbackText() = intent {
        reduce { state.copy(modifiedText = state.originalText) }
    }

    private fun onTextChanged(newText : String) = intent {
        reduce { state.copy(modifiedText = newText) }
    }






}