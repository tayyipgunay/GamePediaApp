package com.tayyipgunay.gamepedia.domain.repository.localgamerepository

import com.tayyipgunay.gamepedia.data.local.GameEntity
import com.tayyipgunay.gamepedia.util.Resource

interface LocalGameRepository {

    // Veritabanındaki tüm oyunları getirir ve sonucu Resource olarak döndürür
    suspend fun getAllGames(): Resource<List<GameEntity>> // GameEntity listesini döndürür

    // Yeni bir oyunu veritabanına ekler ve işlem sonucunu Resource olarak döndürür
    suspend fun insertGame(game: GameEntity): Resource<Unit>

    // Belirtilen ID'ye sahip oyunu veritabanından siler ve işlem sonucunu Resource olarak döndürür
    suspend fun deleteGameById(game: Int): Resource<Unit>

    // Oyunun favorilere eklenip eklenmediğini kontrol eder ve sonucu Resource olarak döndürür
    suspend fun isGameFavorited(gameId: Int): Resource<Boolean> // Resource ile durum yönetimi sağlanır
}


