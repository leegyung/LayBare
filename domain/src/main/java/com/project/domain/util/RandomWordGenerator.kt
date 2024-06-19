package com.project.domain.util

import java.time.LocalDateTime
import kotlin.random.Random

object RandomWordGenerator {
    private val keywords = arrayListOf(
        "Nature",
        "Landscape",
        "Sunset",
        "Portrait",
        "Abstract",
        "Urban",
        "Architecture",
        "Wildlife",
        "Macro",
        "Travel",
        "Vintage",
        "Aerial",
        "Black and White",
        "Minimalist",
        "Silhouette",
        "Night",
        "Food",
        "Sports",
        "Fashion",
        "Art",
        "Nature",
        "Landscape",
        "Sunset",
        "Beach",
        "Mountains",
        "Forest",
        "Waterfall",
        "Wildlife",
        "Cityscape",
        "Skyline",
        "Architecture",
        "Historical site",
        "Monument",
        "Street photography",
        "Portrait",
        "Close-up",
        "Macro photography",
        "Night photography",
        "Long exposure",
        "Black and white",
        "Vintage",
        "Aerial view",
        "Drone photography",
        "Underwater",
        "Abstract",
        "Minimalist",
        "Patterns",
        "Textures",
        "Food photography",
        "Desserts",
        "Cultural festival",
        "Sports",
        "Fashion",
        "Editorial",
        "Wedding",
        "Travel",
        "Adventure",
        "Animals",
        "Birds",
        "Pets",
        "Flowers",
        "Gardens",
        "Autumn",
        "Winter",
        "Spring",
        "Summer",
        "Silhouette",
        "Reflection",
        "Panorama",
        "Macro flowers"
    )

    private fun getRandomInt() : Int {
        val seed = LocalDateTime.now().nano
        val random = Random(seed)
        return random.nextInt(0, keywords.size)
    }

    fun getRandomWord() : String {
        return try{
            keywords[getRandomInt()]
        }catch (_:Exception){
            keywords[0]
        }
    }




}