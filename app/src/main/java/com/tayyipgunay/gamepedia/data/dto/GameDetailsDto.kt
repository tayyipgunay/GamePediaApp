package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
//import com.tayyipgunay.gamepedia.data.Rating
import com.tayyipgunay.gamepedia.domain.model.GameDetail
import com.tayyipgunay.gamepedia.domain.model.Genres

data class GameDetailsDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("released")
    val released: String?,

    @SerializedName("background_image")
    val backgroundImage: String?,

    @SerializedName("rating")
    val rating: Float?,




    @SerializedName("genres")
    val genres: List<GenresDto>,

    @SerializedName("platforms")
    val platforms: List<PlatformDTO>?,

    @SerializedName("tags")
    val tags: List<TagDTO>?,

    @SerializedName("publishers")
    val publishers: List<PublisherDTO>?,

    @SerializedName("esrb_rating")
    val esrbRating: EsrbRatingDTO?,

    @SerializedName("description_raw")
    val description: String?
)

data class TagDTO(
    @SerializedName("name")
    val name: String
)





data class PlatformDTO(
    @SerializedName("platform")
    val name: PlatformNameDTO
)

data class PlatformNameDTO(
    @SerializedName("name")
    val name: String
)

data class PublisherDTO(
    @SerializedName("name")
    val name: String
)
data class EsrbRatingDTO(
    @SerializedName("name")
    val name: String
)



fun GameDetailsDTO.toGameDetail(): GameDetail {
    return GameDetail(
        id = id,
        title = name,
        releaseDate = released ?: "Unknown",
        backgroundImageUrl = backgroundImage ?: "",
        rating = rating?.toString()?:"Unknown",
        genres=genres.map { genres->
            Genres(
                name = genres.genreName
            )

        },

        /*genres = genres?.joinToString(", ") { genres->

            genres.genreName
        } ?: "No genres",*/


    platforms = platforms?.joinToString(", ") {
        platforms->
            platforms.name.name

        } ?: "No platforms",
        tags = tags?.joinToString(", ") { tags->
            tags.name
        } ?: "No tags",
        publisher = publishers?.joinToString(", ") {publishers->
            publishers.name
        } ?: "Unknown publisher",
        esrbRating = esrbRating?.name ?: "Not Rated",
        description = description ?: "Description not available"
    )
}

