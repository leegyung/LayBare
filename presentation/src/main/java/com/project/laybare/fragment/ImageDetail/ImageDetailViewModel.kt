package com.project.laybare.fragment.ImageDetail

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.usecase.SearchLandmarkUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import com.project.data.util.ImageDownloader
import com.project.domain.usecase.ExtractImageLabelUseCase
import com.project.domain.usecase.ExtractTextEntityUseCase
import com.project.domain.usecase.ExtractTextUseCase
import com.project.domain.util.LibraryResult
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val mSearchLandMarkUseCase: SearchLandmarkUseCase,
    private val mExtractTextUseCase: ExtractTextUseCase,
    private val mExtractTextEntityUseCase: ExtractTextEntityUseCase,
    private val mExtractImageLabelUseCase: ExtractImageLabelUseCase
) : ViewModel() {


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

    private val mOptionAdapter = ImageDetailOptionAdapter()

    init{
        mImageUrl = ImageDetailData.getImageUrl()
    }

    fun getImageUrl() : String = mImageUrl

    fun getOptionAdapter() : ImageDetailOptionAdapter = mOptionAdapter

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

    fun extractText(bitmap: Bitmap?, extractEntity : Boolean) {
        viewModelScope.launch {
            if(bitmap == null){
                _createAlert.emit("텍스트 추출 오류")
                return@launch
            }

            mExtractTextUseCase(bitmap).collectLatest { result ->
                when(result){
                    is ApiResult.ResponseLoading -> {
                        _apiLoading.emit(true)
                    }
                    is ApiResult.ResponseSuccess -> {
                        if(extractEntity){
                            extractEntity(result.data)
                        }else{
                            ImageDetailData.setExtractedText(result.data)
                            _apiLoading.emit(false)
                            _textRecognitionResult.emit(true)
                        }
                    }
                    is ApiResult.ResponseError -> {
                        _apiLoading.emit(false)
                        _createAlert.emit(result.errorMessage)
                    }
                }
            }
        }
    }


    private fun extractEntity(extractText : String) {
        mExtractTextEntityUseCase(extractText).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {
                    _apiLoading.emit(true)
                }
                is ApiResult.ResponseSuccess -> {
                    val data = result.data
                    if(data.isNotEmpty()){
                        ImageDetailData.setContactData(data)
                        _contractResult.emit(true)
                    }else{
                        _createAlert.emit("검색된 연락처가 없어요...")
                    }
                    _apiLoading.emit(false)
                }
                is ApiResult.ResponseError -> {
                    _apiLoading.emit(false)
                    _createAlert.emit(result.errorMessage)
                }
            }
        }.launchIn(viewModelScope)
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



    fun extractImageLabel(bitmap: Bitmap?) {
        if(bitmap == null){
            viewModelScope.launch {
                _createAlert.emit("이미지 파일 오류")
            }
            return
        }

        mExtractImageLabelUseCase(bitmap).onEach { result ->
            when(result){
                is LibraryResult.ResponseLoading -> {
                    _apiLoading.emit(true)
                }
                is LibraryResult.ResponseSuccess -> {
                    println(result.data)
                }
                is LibraryResult.ResponseError -> {

                }
            }
        }.launchIn(viewModelScope)

    }





}