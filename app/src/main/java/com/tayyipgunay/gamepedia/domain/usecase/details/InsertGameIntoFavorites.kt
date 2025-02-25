package com.tayyipgunay.gamepedia.domain.usecase.details

import com.tayyipgunay.gamepedia.data.dto.toEntity
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class insertGameIntoFavorites @Inject constructor(private val localGameRepository: LocalGameRepository) {


    suspend fun executeinsertGameIntoFavorites(game: Game): Resource<Unit> {
        val result = localGameRepository.insertGame(game.toEntity())

        when (result) {
            is Resource.Error -> {
                println("ekleme errror içindeyiz")
                return Resource.Error(
                    result.message ?: "An error occurred while fetching game data.")



            }

            is Resource.Loading -> {
                println("ekleme loading içindeyiz")

                return Resource.Loading()

            }

            is Resource.Success -> {
                println("ekleme sucess içindeyiz içindeyiz")

                return Resource.Success(Unit)


            }
        }
    }
}