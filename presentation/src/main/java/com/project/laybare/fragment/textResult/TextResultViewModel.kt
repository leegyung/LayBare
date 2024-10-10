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

    private var mOriginalText = ""
    private var mEditedText = ""



    fun handelEvent(event: TextResultEvent) {
        when(event){
            TextResultEvent.RollBackText -> rollbackText()
            TextResultEvent.MoveToPreviousPage -> moveToPreviousPage()
        }
    }


    private fun moveToPreviousPage() = intent {
        postSideEffect(TextResultSideEffect.MoveToPreviousPage)
    }

    private fun rollbackText() = intent {
        reduce { state.copy(modifiedText = state.originalText) }
    }



    init {
        val extractedText = ImageDetailData.getExtractedText()
        mOriginalText = extractedText
        mEditedText = extractedText
    }

    fun isOriginalTextValid() : Boolean {
        return mOriginalText.isNotEmpty()
    }

    fun setEditedText(text: String) {
        mEditedText = text
    }

    fun getEditedText() : String {
        return mEditedText
    }

    fun resetText() : String {
        mEditedText = mOriginalText
        return mOriginalText
    }



}