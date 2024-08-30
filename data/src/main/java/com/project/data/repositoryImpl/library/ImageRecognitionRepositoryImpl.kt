package com.project.data.repositoryImpl.library

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.project.domain.repository.library.ImageRecognitionRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageRecognitionRepositoryImpl : ImageRecognitionRepository {
    override suspend fun getImageLabelList(image: Bitmap) : List<String> {
        val target = InputImage.fromBitmap(image, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        val result = suspendCoroutine { continuation ->
            labeler.process(target)
                .addOnSuccessListener { labels ->

                    val labelList = labels.filter { it.confidence > 0.6f }.map { it.text }
                    continuation.resume(labelList)
                }
                .addOnFailureListener { _ ->

                    continuation.resume(emptyList<String>())
                }
        }

        return result
    }
}