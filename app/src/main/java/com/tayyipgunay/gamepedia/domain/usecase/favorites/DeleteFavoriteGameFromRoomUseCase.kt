package com.tayyipgunay.gamepedia.domain.usecase.favorites

import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class deleteFavoriteGameFromRoomUseCase @Inject constructor(private val localGameRepository: LocalGameRepository) {

    suspend fun executeDeleteFavoriteGame(gameId:Int): Resource<Unit> {

        val result = localGameRepository.deleteGameById(gameId)

        when (result) {
            is Resource.Error -> {
                return Resource.Error(
                    result.message ?: "An error occurred while fetching game data."
                )

            }

            is Resource.Loading -> {
                return Resource.Loading()
            }

            is Resource.Success -> {
                return Resource.Success(Unit)

            }


        }
    }
}