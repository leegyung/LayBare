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

class PhotoTaker(private val activity: Activity) {
    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }

    private lateinit var currentPhotoUri: Uri


    fun checkCameraPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한을 요청
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            return false
        }
        return true
    }

    fun dispatchTakePictureIntent(resultListener : ActivityResultLauncher<Uri>) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val resolver = activity.contentResolver
        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            currentPhotoUri = uri
            resultListener.launch(uri)
        }
    }

    fun getPhotoUri() : Uri {
        return currentPhotoUri
    }
}