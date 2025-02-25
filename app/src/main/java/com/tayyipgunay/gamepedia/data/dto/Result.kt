package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
//import com.tayyipgunay.gamepedia.data.Rating

// Her bir oyunun detaylarını temsil eden sınıf.
data class Result(
    @SerializedName("id")
    val id: Int, // Oyunun benzersiz kimliği
    @SerializedName("slug")
    val slug: String, // Oyunun URL dostu kısa adı
    @SerializedName("name")
    val name: String, // Oyunun tam adı
    @SerializedName("released")
    val released: String?, // Oyunun çıkış tarihi
    @SerializedName("tba")
    val tba: Boolean, // Çıkış tarihi belirlenmemişse true
    @SerializedName("background_image")
    val backgroundImage: String?, // Oyunun arka plan resmi URL'si
    @SerializedName("rating")
    val rating: Float?, // Oyunun genel değerlendirme puanı
    @SerializedName("rating_top")
    val ratingTop: Int?, // En yüksek değerlendirme puanı



    @SerializedName("genres")
val genres: List<GenresDto> // Oyun türleri
)
