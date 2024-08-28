package com.project.data.repositoryImpl.library

import android.graphics.Bitmap
import com.google.mlkit.nl.entityextraction.EntityAnnotation
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractionParams
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.project.domain.repository.library.TextRecognitionRepository
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionRepositoryImpl : TextRecognitionRepository {

    override suspend fun extractText(bitmap: Bitmap): String? {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        val result = suspendCoroutine { continuation ->
            recognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    val fullText = text.text
                    if(fullText.isEmpty()){
                        continuation.resume(null)
                    }else{
                        continuation.resume(fullText)
                    }
                }
                .addOnFailureListener { _ ->
                    continuation.resume(null)
                }
        }

        return result
    }


    override suspend fun extractTextEntity(text: String): HashMap<String, ArrayList<String>>? {
        val entityExtractor = EntityExtraction.getClient(EntityExtractorOptions
            .Builder(EntityExtractorOptions.KOREAN).build())

        val result = suspendCoroutine { continuation ->
            entityExtractor
                .downloadModelIfNeeded()
                // 모델 다운로드 성공
                .addOnSuccessListener { _ ->
                    val params = EntityExtractionParams
                        .Builder(text)
                        .setEntityTypesFilter(setOf(8, 3))
                        .setPreferredLocale(Locale.KOREAN)
                        .build()

                    entityExtractor.annotate(params)
                        // 텍스트 엔티티 추출 성공
                        .addOnSuccessListener { result ->
                            val contactData = switchToContactData(result)
                            continuation.resume(contactData)
                        }
                        // 텍스트 엔티티 추출 실패
                        .addOnFailureListener { _ ->
                            continuation.resume(null)
                        }
                }
                // 모델 다운로드 실패
                .addOnFailureListener { _ ->
                    continuation.resume(null)
                }
        }
        return result
    }


    private fun switchToContactData(extractedEntity : List<EntityAnnotation>) : HashMap<String, ArrayList<String>> {
        val result = hashMapOf<String, ArrayList<String>>()

        extractedEntity.forEach { entity ->
            val type = entity.entities.getOrNull(0)?.type
            val key = when (type) {
                8 -> "NUMBER"
                3 -> "EMAIL"
                else -> null
            }

            key?.let {
                result.getOrPut(it) { arrayListOf() }.add(entity.annotatedText)
            }
        }
        return result
    }


}
