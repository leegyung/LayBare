package com.project.laybare.textResult

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextResultViewModel @Inject constructor() : ViewModel() {
    private var mTextResult = ""
    private var mEditedText = ""

    fun setTextResult(text : String) {
        mTextResult = text
        mEditedText = text
    }

    fun setEditedText(text: String) {
        mEditedText = text
    }

    fun getEditedText() : String {
        return mEditedText
    }

    fun resetText() : String {
        mEditedText = mTextResult
        return mTextResult
    }
}