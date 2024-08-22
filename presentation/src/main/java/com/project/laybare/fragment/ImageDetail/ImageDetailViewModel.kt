package com.project.laybare.fragment.ImageDetail

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractionParams
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.project.domain.entity.SearchLandmarkEntity
import com.project.domain.usecase.SearchLandmarkUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import com.project.data.util.ImageDownloader
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.ContractCreator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(private val mSearchLandMarkUseCase: SearchLandmarkUseCase) : ViewModel() {
    private val _createAlert = MutableSharedFlow<String>()
    private val _apiLoading = MutableSharedFlow<Boolean>()
    private val _textRecognitionResult = MutableSharedFlow<Boolean>()
    private val _landmarkResult = MutableSharedFlow<Boolean>()
    private val _contractResult = MutableSharedFlow<Boolean>()
    val mCreateAlert = _createAlert.asSharedFlow()
    val mApiLoading = _apiLoading.asSharedFlow()
    val mTextRecognitionResult = _textRecognitionResult.asSharedFlow()
    val mLandmarkResult = _landmarkResult.asSharedFlow()
    val mContractResult = _contractResult.asSharedFlow()

    private var mImageUrl = ""

    init{
        mImageUrl = ImageDetailData.getImageUrl()
    }

    fun getImageUrl() : String = mImageUrl

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


    fun extractText(bitmap: Bitmap?, extractContact : Boolean) {
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
                    val fullText = text.text
                    if(fullText.isEmpty()){
                        viewModelScope.launch {
                            _createAlert.emit("텍스트 없음")
                        }
                    }else{
                        if(extractContact){
                            extractEntity(fullText)
                        }else{
                            viewModelScope.launch {
                                ImageDetailData.setExtractedText(fullText)
                                _textRecognitionResult.emit(true)
                            }
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

    private fun extractEntity(extractText : String) {
        val entityExtractor = EntityExtraction.getClient(EntityExtractorOptions.Builder(EntityExtractorOptions.KOREAN).build())

        entityExtractor
            .downloadModelIfNeeded()
            .addOnSuccessListener { _ ->
                val params = EntityExtractionParams
                    .Builder(extractText)
                    .setEntityTypesFilter(setOf(8, 3))
                    .setPreferredLocale(Locale.KOREAN)
                    .build()

                entityExtractor.annotate(params)
                    .addOnSuccessListener { result ->
                        viewModelScope.launch {
                            val contactData = ContractCreator().switchToContactData(result)
                            if(contactData.isNotEmpty()){
                                ImageDetailData.setContactData(contactData)
                                _contractResult.emit(true)
                            }else{
                                _createAlert.emit("검색된 연락처가 없어요...")
                            }
                        }
                    }
                    .addOnFailureListener {  e ->

                    }
            }
            .addOnFailureListener { _ ->

            }
    }




    fun getLandmarkData(bitmap: Bitmap?) {
        if(bitmap == null){
            viewModelScope.launch {
                _createAlert.emit("장소 이미지 파일 오류")
            }
            return
        }

        mSearchLandMarkUseCase(BuildConfig.API_KEY, bitmap).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {
                    _apiLoading.emit(true)
                }
                is ApiResult.ResponseSuccess -> {
                    ImageDetailData.setLocationData(result.data)
                    _apiLoading.emit(false)
                    _landmarkResult.emit(true)
                }
                is ApiResult.ResponseError -> {
                    _createAlert.emit(result.errorMessage?:"위치 찾기 오류")
                    _apiLoading.emit(false)
                }
            }
        }.launchIn(viewModelScope)
    }





}