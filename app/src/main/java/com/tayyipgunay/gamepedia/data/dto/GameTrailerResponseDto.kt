package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.domain.model.GameTrailer

data class GameTrailerResponseDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("results")
    val results: List<GameTrailerDto>
)

data class GameTrailerDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("preview")
    val previewImageUrl: String,
    @SerializedName("data")
    val videoData: VideoDataDto
)

data class VideoDataDto(
    @SerializedName("max")
    val maxVideoUrl: String,
    @SerializedName("480")
    val video480pUrl: String
)
fun GameTrailerResponseDto.toGameTrailers(): List<GameTrailer> {
    return results.map { result ->
        GameTrailer(
            videoUrl = result.videoData.maxVideoUrl
        )
    }
}
