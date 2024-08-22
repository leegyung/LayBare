package com.project.laybare.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionChecker {
    companion object{
        private const val CONTACT_PERMISSION_CODE = 100
    }
    fun checkContactPermission(context: Context, launcher : ActivityResultLauncher<String>) : Boolean{
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.WRITE_CONTACTS)
            return true
        }
        return false
    }
}