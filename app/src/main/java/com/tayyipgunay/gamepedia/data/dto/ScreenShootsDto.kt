package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.domain.model.Screenshot


// API'den gelen ekran görüntüsü (screenshot) verilerini temsil eden veri sınıfı.
data class ScreenShootsDto(
    @SerializedName("count") // JSON'daki "count" alanını eşler.
    val count: Int, // Toplam ekran görüntüsü sayısı.

    @SerializedName("results") // JSON'daki "results" alanını eşler.
    val results: List<Screenshoots> // Ekran görüntülerinin detaylarını içeren liste.
)

// Tek bir ekran görüntüsünü temsil eden veri sınıfı.
data class Screenshoots(
    @SerializedName("id") // JSON'daki "id" alanını eşler.
    val id: Int, // Ekran görüntüsü ID'si.

    @SerializedName("image") // JSON'daki "image" alanını eşler.
    val image: String // Ekran görüntüsü URL'si.
)

// ScreenShootsDto'yu domain katmanındaki List<Screenshot> türüne dönüştürmek için extension fonksiyonu.
fun ScreenShootsDto.toScreenshotDomainModel(): List<Screenshot> {
    return results.map { screenshoots -> // Her bir Screenshoots öğesini Screenshot'a dönüştür.
        Screenshot(
            imageUrl = screenshoots.image // Ekran görüntüsü URL'sini kullan.
        )
    }
}
