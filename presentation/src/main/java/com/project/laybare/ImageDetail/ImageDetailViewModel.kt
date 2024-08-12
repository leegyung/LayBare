package com.project.laybare.ImageDetail

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.project.data.util.ImageDownloader
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.usecase.SearchLandmarkUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(private val mSearchLandMarkUseCase: SearchLandmarkUseCase) : ViewModel() {
    private val _createAlert = MutableSharedFlow<String>()
    private val _textRecognitionResult = MutableSharedFlow<String>()
    private val _landmarkResult = MutableSharedFlow<SearchLandmarkEntity?>()
    val mCreateAlert = _createAlert.asSharedFlow()
    val mTextRecognitionResult = _textRecognitionResult.asSharedFlow()
    val mLandmarkResult = _landmarkResult.asSharedFlow()

    private var mType = ""
    private var mImageUrl = ""
    private var mImageUri : Uri? = null


    fun requireImageDataSetting() : Boolean {
        return mImageUrl.isEmpty() && mType.isEmpty() && mImageUri == null
    }

    fun setImageData(type : String, url : String, uri: Uri) {
        mType = type
        mImageUrl = url
        mImageUri = uri
    }


    fun isUrlType() : Boolean {
        return mImageUrl.isNotEmpty()
    }

    fun getImageUrl() : String {
        return mImageUrl
    }

    fun getImageUri() : Uri? {
        return mImageUri
    }



    fun downloadImage(context: Context) {
        viewModelScope.launch {
            val downloadManger = ImageDownloader(context)
            val date = LocalDateTime.now()
            val name = date.second.toString() + date.nano.toString()
            if(downloadManger.downloadAndSaveImage(mImageUrl, name)){
                _createAlert.emit("이미지 다운로드 완료")
            }else{
                _createAlert.emit("이미지 다운로드 실패")
            }
        }
    }


    fun extractText(bitmap: Bitmap?) {
        if(bitmap == null){
            viewModelScope.launch {
                _createAlert.emit("텍스트 추출 오류")
            }
            return
        }

        viewModelScope.launch {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
            recognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    if(text.text.isEmpty()){
                        viewModelScope.launch {
                            _createAlert.emit("텍스트 없음")
                        }
                    }else{
                        viewModelScope.launch {
                            _textRecognitionResult.emit(text.text)
                        }

                    }
                }
                .addOnFailureListener { _ ->
                    viewModelScope.launch {
                        _createAlert.emit("텍스트 추출 오류")
                    }
                }
        }


    }


    fun getLocationData(bitmap: Bitmap?) {
        if(bitmap == null){
            return
        }

        mSearchLandMarkUseCase(BuildConfig.API_KEY, bitmap).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {

                }
                is ApiResult.ResponseSuccess -> {
                    _landmarkResult.emit(result.data)
                }
                is ApiResult.ResponseError -> {
                    _createAlert.emit(result.errorMessage?:"위치 찾기 오류")
                }
            }
        }.launchIn(viewModelScope)
    }




}