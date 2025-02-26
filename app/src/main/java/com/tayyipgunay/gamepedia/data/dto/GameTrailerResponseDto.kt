package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.domain.model.GameTrailer

// API'den gelen oyun fragmanı (trailer) verilerini temsil eden veri sınıfı.
data class GameTrailerResponseDto(
    @SerializedName("count") // JSON'daki "count" alanını eşler.
    val count: Int, // Toplam fragman sayısı.

    @SerializedName("results") // JSON'daki "results" alanını eşler.
    val results: List<GameTrailerDto> // Fragmanların detaylarını içeren liste.
)

// Tek bir oyun fragmanını temsil eden veri sınıfı.
data class GameTrailerDto(
    @SerializedName("id") // JSON'daki "id" alanını eşler.
    val id: Int, // Fragman ID'si.

    @SerializedName("name") // JSON'daki "name" alanını eşler.
    val name: String, // Fragman adı.

    @SerializedName("preview") // JSON'daki "preview" alanını eşler.
    val previewImageUrl: String, // Fragman önizleme resmi URL'si.

    @SerializedName("data") // JSON'daki "data" alanını eşler.
    val videoData: VideoDataDto // Fragman video URL'lerini içeren iç içe veri sınıfı.
)

// Fragman video URL'lerini temsil eden veri sınıfı.
data class VideoDataDto(
    @SerializedName("max") // JSON'daki "max" alanını eşler.
    val maxVideoUrl: String, // En yüksek kalitedeki video URL'si.

    @SerializedName("480") // JSON'daki "480" alanını eşler.
    val video480pUrl: String // 480p kalitesindeki video URL'si.
)

// GameTrailerResponseDto'yu domain katmanındaki List<GameTrailer> türüne dönüştürmek için extension fonksiyonu.
fun GameTrailerResponseDto.toGameTrailers(): List<GameTrailer> {
    return results.map { result -> // Her bir GameTrailerDto öğesini GameTrailer'e dönüştür.
        GameTrailer(
            videoUrl = result.videoData.maxVideoUrl // En yüksek kalitedeki video URL'sini kullan.
        )
    }
}