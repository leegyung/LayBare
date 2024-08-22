package com.project.laybare.fragment.textResult

import androidx.lifecycle.ViewModel
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextResultViewModel @Inject constructor() : ViewModel() {
    private var mOriginalText = ""
    private var mEditedText = ""

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