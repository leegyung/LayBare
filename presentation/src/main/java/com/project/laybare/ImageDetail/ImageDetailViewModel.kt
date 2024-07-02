package com.project.laybare.ImageDetail

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.project.data.util.ImageDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor() : ViewModel() {
    private val _createToast = MutableStateFlow("")
    private val _textRecognitionResult = MutableStateFlow("")
    val mCreateToast get() = _createToast
    val mTextRecognitionResult get() = _textRecognitionResult

    private var mImageUrl = ""
    private var mThumbnailUrl = ""

    fun requireUrlSetting() : Boolean {
        return mImageUrl.isEmpty() && mThumbnailUrl.isEmpty()
    }

    fun setImageUrl(url : String, thumbnail : String) {
        mImageUrl = url
        mThumbnailUrl = thumbnail
    }

    fun getImageUrl() : String {
        return mImageUrl
    }

    fun getThumbnail() : String {
        return mThumbnailUrl
    }

    fun downloadImage(context: Context) {
        viewModelScope.launch {
            val downloadManger = ImageDownloader(context)
            val date = LocalDateTime.now()
            val name = date.second.toString() + date.nano.toString()
            if(downloadManger.downloadAndSaveImage(mImageUrl, name)){
                _createToast.value = "이미지 다운로드 완료"
            }else{
                if(downloadManger.downloadAndSaveImage(mThumbnailUrl, name)){
                    _createToast.value = "이미지 다운로드 완료"
                }else{
                    _createToast.value = "이미지 다운로드 애러"
                }
            }
        }
    }


    fun extractText(bitmap: Bitmap?) {
        if(bitmap == null){
            _createToast.value = "텍스트 추출 오류"
            return
        }

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage)
            .addOnSuccessListener { text ->
                var resultStr = ""
                for (i in text.textBlocks){
                    resultStr += (i.text + "\n")
                }
                _textRecognitionResult.value = resultStr
            }
            .addOnFailureListener { e ->
                _createToast.value = "텍스트 추출 오류"
            }
    }


}