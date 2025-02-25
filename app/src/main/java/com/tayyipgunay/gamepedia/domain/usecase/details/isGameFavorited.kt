package com.tayyipgunay.gamepedia.domain.usecase.details

import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class isGameFavoritedUseCase @Inject constructor(private val localGameRepository: LocalGameRepository) {


    suspend fun execute(gameId: Int): Resource<Boolean> {
        val result = localGameRepository.isGameFavorited(gameId)
        return when (result) {
            is Resource.Success -> {
                Resource.Success(result.data!!)

            }

            is Resource.Error -> {
                Resource.Error(
                    result.message ?: "An error occurred while checking if the game is favorited."
                )
            }

            is Resource.Loading -> {
                Resource.Loading()
            }
        }


    }
}