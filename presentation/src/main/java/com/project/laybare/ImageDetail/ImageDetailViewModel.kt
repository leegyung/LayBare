package com.project.laybare.ImageDetail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.util.ImageDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor() : ViewModel() {
    private val _createToast = MutableStateFlow("")
    val mCreateToast get() = _createToast

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


}