package com.tayyipgunay.gamepedia.domain.repository.localgamerepository

import com.tayyipgunay.gamepedia.data.local.GameEntity
import com.tayyipgunay.gamepedia.util.Resource

interface LocalGameRepository {
    suspend fun getAllGames(): Resource<List<GameEntity>>//GameEntity
    suspend fun insertGame(game: GameEntity): Resource<Unit>
    suspend fun deleteGameById(game: Int): Resource<Unit>
    suspend fun isGameFavorited(gameId: Int): Resource<Boolean> // Resource ile durum yönetimi

}


