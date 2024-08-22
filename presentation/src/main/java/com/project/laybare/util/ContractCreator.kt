package com.project.laybare.util

import com.google.mlkit.nl.entityextraction.EntityAnnotation

class ContractCreator {

    fun switchToContactData(extractedEntity : List<EntityAnnotation>) : HashMap<String, ArrayList<String>> {
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