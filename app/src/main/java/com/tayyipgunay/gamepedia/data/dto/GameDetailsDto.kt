package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
//import com.tayyipgunay.gamepedia.data.Rating
import com.tayyipgunay.gamepedia.domain.model.GameDetail
import com.tayyipgunay.gamepedia.domain.model.Genres

// Oyun detaylarını temsil eden veri sınıfı.
// API'den gelen JSON verilerini bu sınıfa eşler.
data class GameDetailsDTO(
    @SerializedName("id") // JSON'daki "id" alanını eşler.
    val id: Int,

    @SerializedName("name") // JSON'daki "name" alanını eşler.
    val name: String,

    @SerializedName("released") // JSON'daki "released" alanını eşler.
    val released: String?, // Oyunun çıkış tarihi (nullable).

    @SerializedName("background_image") // JSON'daki "background_image" alanını eşler.
    val backgroundImage: String?, // Oyunun arka plan resmi URL'si (nullable).

    @SerializedName("rating") // JSON'daki "rating" alanını eşler.
    val rating: Float?, // Oyunun puanı (nullable).

    @SerializedName("genres") // JSON'daki "genres" alanını eşler.
    val genres: List<GenresDto>, // Oyunun türlerini içeren liste.

    @SerializedName("platforms") // JSON'daki "platforms" alanını eşler.
    val platforms: List<PlatformDTO>?, // Oyunun platformlarını içeren liste (nullable).

    @SerializedName("tags") // JSON'daki "tags" alanını eşler.
    val tags: List<TagDTO>?, // Oyunun etiketlerini içeren liste (nullable).

    @SerializedName("publishers") // JSON'daki "publishers" alanını eşler.
    val publishers: List<PublisherDTO>?, // Oyunun yayıncılarını içeren liste (nullable).

    @SerializedName("esrb_rating") // JSON'daki "esrb_rating" alanını eşler.
    val esrbRating: EsrbRatingDTO?, // Oyunun ESRB derecelendirmesi (nullable).

    @SerializedName("description_raw") // JSON'daki "description_raw" alanını eşler.
    val description: String? // Oyunun açıklaması (nullable).
)

// Oyun etiketlerini temsil eden veri sınıfı.
data class TagDTO(
    @SerializedName("name") // JSON'daki "name" alanını eşler.
    val name: String // Etiket adı.
)

// Oyun platformlarını temsil eden veri sınıfı.
data class PlatformDTO(
    @SerializedName("platform") // JSON'daki "platform" alanını eşler.
    val name: PlatformNameDTO // Platform adını içeren iç içe veri sınıfı.
)

// Platform adını temsil eden veri sınıfı.
data class PlatformNameDTO(
    @SerializedName("name") // JSON'daki "name" alanını eşler.
    val name: String // Platform adı.
)

// Oyun yayıncılarını temsil eden veri sınıfı.
data class PublisherDTO(
    @SerializedName("name") // JSON'daki "name" alanını eşler.
    val name: String // Yayıncı adı.
)

// Oyunun ESRB derecelendirmesini temsil eden veri sınıfı.
data class EsrbRatingDTO(
    @SerializedName("name") // JSON'daki "name" alanını eşler.
    val name: String // ESRB derecelendirme adı.
)

// GameDetailsDTO'yu domain katmanındaki GameDetail sınıfına dönüştüren extension fonksiyon.
fun GameDetailsDTO.toGameDetail(): GameDetail {
    return GameDetail(
        id = id, // Oyun ID'si.
        title = name, // Oyun adı.
        releaseDate = released ?: "Unknown", // Çıkış tarihi (null ise "Unknown").
        backgroundImageUrl = backgroundImage ?: "", // Arka plan resmi URL'si (null ise boş string).
        rating = rating?.toString() ?: "Unknown", // Oyun puanı (null ise "Unknown").
        genres = genres.map { genres -> // Türleri domain katmanındaki Genres sınıfına dönüştür.
            Genres(
                name = genres.genreName // Tür adı.
            )
        },
        platforms = platforms?.joinToString(", ") { platforms -> // Platformları birleştir.
            platforms.name.name
        } ?: "No platforms", // Null ise "No platforms".
        tags = tags?.joinToString(", ") { tags -> // Etiketleri birleştir.
            tags.name
        } ?: "No tags", // Null ise "No tags".
        publisher = publishers?.joinToString(", ") { publishers -> // Yayıncıları birleştir.
            publishers.name
        } ?: "Unknown publisher", // Null ise "Unknown publisher".
        esrbRating = esrbRating?.name ?: "Not Rated", // ESRB derecelendirmesi (null ise "Not Rated").
        description = description ?: "Description not available" // Açıklama (null ise "Description not available").
    )
}