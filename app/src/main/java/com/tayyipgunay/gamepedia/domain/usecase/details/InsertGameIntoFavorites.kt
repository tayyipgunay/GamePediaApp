package com.tayyipgunay.gamepedia.domain.usecase.details

import com.tayyipgunay.gamepedia.data.dto.toEntity
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class insertGameIntoFavorites @Inject constructor(private val localGameRepository: LocalGameRepository) {


    suspend fun executeinsertGameIntoFavorites(game: Game): Resource<Unit> {
        // Oyunu GameEntity formatına dönüştürüp veritabanına ekler
        val result = localGameRepository.insertGame(game.toEntity())

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Error -> {
                println("Ekleme işlemi sırasında hata oluştu")
                return Resource.Error(
                    result.message ?: "An error occurred while inserting the game into favorites."
                )
            }

            is Resource.Loading -> {
                println("Ekleme işlemi devam ediyor")
                return Resource.Loading()
            }

            is Resource.Success -> {
                println("Ekleme işlemi başarılı")
                return Resource.Success(Unit) // Başarılı işlem sonucunda Unit döndürülür
            }
        }
    }
}
