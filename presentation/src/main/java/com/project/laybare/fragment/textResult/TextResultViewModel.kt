package com.project.laybare.fragment.textResult

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextResultViewModel @Inject constructor() : ViewModel() {
    private var mOriginalText = ""
    private var mEditedText = ""

    fun requireTextData() : Boolean {
        return mOriginalText.isEmpty() && mEditedText.isEmpty()
    }

    fun setTextResult(text : String) {
        mOriginalText = text
        mEditedText = text
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