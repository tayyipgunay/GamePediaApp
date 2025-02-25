package com.tayyipgunay.gamepedia.domain.model

data class GameDetail(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val backgroundImageUrl: String,
    val rating: String,
    val genres: List<Genres> , // Oyunun türleri

    // val genres: String,
    val platforms: String,
    val tags: String,
    val publisher: String,
    val esrbRating: String,
    val description: String
)
