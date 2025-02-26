package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.data.local.GameEntity
import com.tayyipgunay.gamepedia.data.local.gameEntitytoDomain
import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.model.Genres

// Ana DTO sınıfı, API'den gelen tüm oyun verilerini temsil eder.
// API'den gelen oyun listesi verilerini temsil eden veri sınıfı.
data class GameDto(
    @SerializedName("count") // JSON'daki "count" alanını eşler.
    val count: Int, // Toplam oyun sayısı.

    @SerializedName("next") // JSON'daki "next" alanını eşler.
    val next: String?, // Sonraki sayfanın URL'si (sayfalandırma için, nullable).

    @SerializedName("previous") // JSON'daki "previous" alanını eşler.
    val previous: String?, // Önceki sayfanın URL'si (sayfalandırma için, nullable).

    @SerializedName("results") // JSON'daki "results" alanını eşler.
    val results: List<Result> // Oyunların detaylarını içeren liste.
)

// GameDto'yu domain katmanındaki List<Game> türüne dönüştürmek için extension fonksiyonu.
fun GameDto.toGameDomainModel(): List<Game> {
    return results.map { result -> // Her bir Result öğesini Game'e dönüştür.
        Game(
            id = result.id, // Oyun ID'si.
            slug = result.slug, // Oyun slug'ı.
            name = result.name, // Oyun adı.
            released = result.released, // Oyunun çıkış tarihi.
            backgroundImage = result.backgroundImage, // Oyunun arka plan resmi URL'si.
            rating = result.rating, // Oyunun puanı.
            genres = result.genres.map { genresDto -> // Türleri domain katmanındaki Genres sınıfına dönüştür.
                Genres(
                    name = genresDto.genreName // Tür adı.
                )
            }
        )
    }
}

// GameDto'yu domain katmanındaki List<Banner> türüne dönüştürmek için extension fonksiyonu.
fun GameDto.toBannerDomainModel(): List<Banner> {
    return results.map { result -> // Her bir Result öğesini Banner'a dönüştür.
        Banner(
            backgroundImage = result.backgroundImage // Banner için arka plan resmi URL'si.
        )
    }
}

// Game domain modelini veritabanı katmanındaki GameEntity'ye dönüştürmek için extension fonksiyonu.
fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = this.id, // Oyun ID'si.
        name = this.name, // Oyun adı.
        released = this.released, // Oyunun çıkış tarihi.
        backgroundImage = this.backgroundImage, // Oyunun arka plan resmi URL'si.
        rating = this.rating, // Oyunun puanı.
        slug = this.slug, // Oyun slug'ı.
        genres = this.genres.map { genres -> // Türleri veritabanı katmanındaki Genres sınıfına dönüştür.
            Genres(
                name = genres.name // Tür adı.
            )
        }
    )
}






