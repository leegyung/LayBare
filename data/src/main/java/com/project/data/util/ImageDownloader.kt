package com.project.data.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import com.bumptech.glide.request.target.CustomTarget

class ImageDownloader(private val context: Context) {

    suspend fun downloadAndSaveImage(url: String, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .override(CustomTarget.SIZE_ORIGINAL, CustomTarget.SIZE_ORIGINAL)
                    .submit()
                    .get()

                // MediaStore에 저장
                saveImageToMediaStore(bitmap, fileName)
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }




    private fun saveImageToMediaStore(bitmap: Bitmap, fileName: String): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: return false

        return try {
            resolver.openOutputStream(uri).use { outputStream ->
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }

            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

    }


}