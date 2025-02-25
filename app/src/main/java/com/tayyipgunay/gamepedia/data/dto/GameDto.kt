package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.data.local.GameEntity
import com.tayyipgunay.gamepedia.data.local.gameEntitytoDomain
import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.model.Genres

// Ana DTO sınıfı, API'den gelen tüm oyun verilerini temsil eder.
data class GameDto(
    @SerializedName("count")
    val count: Int, // Toplam oyun sayısı
    @SerializedName("next")
    val next: String?, // Sonraki sayfanın URL'si (sayfalandırma için)
    @SerializedName("previous")
    val previous: String?, // Önceki sayfanın URL'si (sayfalandırma için)
    @SerializedName("results")
    val results: List<Result> // Oyunların detaylarını içeren liste
)


// GameDto'yu List<Game> türüne dönüştürmek için extension fonksiyonu.
fun GameDto.toGameDomainModel(): List<Game> {
    return results.map { result ->
        Game(
            id = result.id,
            slug = result.slug,
            name = result.name,
            released = result.released,
            backgroundImage = result.backgroundImage,
            rating = result.rating,

            genres = result.genres.map { genresDto ->

                Genres(
                    name = genresDto.genreName
                )
            }
        )


    }
}
fun GameDto.toBannerDomainModel(): List<Banner> {
    return results.map { result ->
        Banner(
            backgroundImage = result.backgroundImage
        )
    }
}
// Game -> GameEntity
fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = this.id,
        name = this.name,
        released = this.released,
        backgroundImage = this.backgroundImage,
        rating = this.rating,
        slug = this.slug,
        genres = this.genres.map { genres ->
            Genres(
                name = genres.name
            )

      /*  genres = this.genres.joinToString(",") {genres->
            genres.name
*/

        }
    )
}








