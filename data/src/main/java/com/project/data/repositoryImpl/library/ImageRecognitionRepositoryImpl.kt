package com.project.data.repositoryImpl.library

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.project.domain.entity.ImageLabelEntity
import com.project.domain.repository.library.ImageRecognitionRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageRecognitionRepositoryImpl : ImageRecognitionRepository {
    override suspend fun getImageLabelList(image : ByteArray) : List<ImageLabelEntity>? {
        // byteArray 비트맵 변환
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        val result = suspendCoroutine { continuation ->
            labeler.process(inputImage)
                .addOnSuccessListener { labels ->

                    val labelList = labels.filter { it.confidence > 0.6f }.map { ImageLabelEntity(it.text) }
                    continuation.resume(labelList)
                }
                .addOnFailureListener { _ ->

                    continuation.resume(null)
                }
        }

        return result
    }
}