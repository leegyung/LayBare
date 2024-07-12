package com.project.laybare.ImageDetail

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.project.data.util.ImageDownloader
import com.project.domain.entity.LandmarkEntity
import com.project.domain.usecase.SearchLandmarkUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(private val mUseCase: SearchLandmarkUseCase) : ViewModel() {
    private val _createToast = MutableSharedFlow<String>()
    private val _textRecognitionResult = MutableStateFlow("")
    private val _landmarkResult = MutableStateFlow<LandmarkEntity?>(null)
    val mCreateToast = _createToast.asSharedFlow()
    val mTextRecognitionResult get() = _textRecognitionResult
    val mLandmarkResult get() = _landmarkResult

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
                _createToast.emit("이미지 다운로드 완료")
            }else{
                _createToast.emit("이미지 다운로드 완료")
            }
        }
    }


    fun extractText(bitmap: Bitmap?) {
        if(bitmap == null){
            viewModelScope.launch {
                _createToast.emit("텍스트 추출 오류")
            }
            return
        }

        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        recognizer.process(inputImage)
            .addOnSuccessListener { text ->
                if(text.text.isEmpty()){
                    viewModelScope.launch {
                        _createToast.emit("텍스트 없음")
                    }
                }else{
                    _textRecognitionResult.value = text.text
                }
            }
            .addOnFailureListener { _ ->
                viewModelScope.launch {
                    _createToast.emit("텍스트 추출 오류")
                }
            }
    }


    fun getLocationData(bitmap: Bitmap?) {

        viewModelScope.launch {
            if(bitmap == null){
                _createToast.emit("랜드마크 사진 오류")
                return@launch
            }


            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

            mUseCase.getLandmarkLocation(BuildConfig.API_KEY, base64Image, 1).collectLatest { result ->
                if(result is ApiResult.ResponseSuccess){
                    _landmarkResult.value = result.data
                }else{
                    _createToast.emit("위치 데이터 오류")
                }

            }
        }

    }




}