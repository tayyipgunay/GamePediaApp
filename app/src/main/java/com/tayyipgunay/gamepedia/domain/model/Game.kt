package com.tayyipgunay.gamepedia.domain.model

import com.tayyipgunay.gamepedia.data.dto.GenresDto

// Domain katmanında kullanılan sade oyun modeli.
data class Game(
    val id: Int, // Oyunun benzersiz kimliği
    val slug: String, // Oyunun URL dostu kısa adı
    val name: String, // Oyunun adı
    val released: String?, // Oyunun çıkış tarihi
    val backgroundImage: String?, // Oyunun arka plan resmi URL'si
    val rating: Float?, // Oyunun genel değerlendirme puanı



    val genres: List<Genres>  // Oyunun türleri
)
