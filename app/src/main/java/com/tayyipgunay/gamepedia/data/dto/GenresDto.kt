package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.domain.model.Genres

data class GenresDto(
    @SerializedName("id")
    val id: Int, // Oyun türünün benzersiz kimliği
    @SerializedName("name")
    val genreName: String, // Oyun türünün adı
    val slug: String, // Oyun türünün URL dostu kısa adı
    val games_count: Int // Oyun türüne ait oyun sayısı


)



