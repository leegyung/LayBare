package com.project.laybare.util

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PhotoTaker(private val mContext: Context) {

    // 사진 촬영을 위해 만든 파일의 uri
    private var currentPhotoUri: Uri? = null

    /**
     * 카메라 어플 사용 권한 체크
     * 권한이 없다면 ActivityResultLauncher 를 통해 요청
     */
    fun checkCameraPermission(launcher : ActivityResultLauncher<String>) : Boolean {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한을 요청
            launcher.launch(Manifest.permission.CAMERA)
            return false
        }
        return true
    }

    /**
     * 촬영을 위해 파일을 생성
     * ActivityResultLauncher에 생성한 uri를 전달 후 카메라 어플 실행
     */
    fun dispatchTakePictureIntent(resultListener : ActivityResultLauncher<Uri>) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val resolver = mContext.contentResolver
        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            currentPhotoUri = uri
            resultListener.launch(uri)
        }
    }

    fun getPhotoUri() : Uri? {
        return currentPhotoUri
    }
}