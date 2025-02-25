package com.tayyipgunay.gamepedia.domain.usecase.details

import com.tayyipgunay.gamepedia.data.dto.toGameDetail
import com.tayyipgunay.gamepedia.domain.model.GameDetail
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.DetailsRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class GetGameDetailsUseCase @Inject constructor(private val detailsRepository: DetailsRepository) {
   /* suspend fun executeGetGameDetailsUseCase(gameId: Int): GameDetail {
        val response = gameRepository.getGameDetails(gameId)
        if (response.isSuccessful) {
            val gameDetails = response.body()?.toGameDetail()
            return gameDetails ?: throw Exception("Game details not found")
        } else {
            throw Exception("API Error: ${response.code()} - ${response.message()}")
        }

    }*/

    suspend fun executeGetGameDetailsUseCase(gameId: Int): Resource<GameDetail>{
        val result=detailsRepository.getGameDetails(gameId)

       return when(result){
            is Resource.Success -> {
                Resource.Success(result.data!!.toGameDetail())
            }

            is Resource.Error -> {
                Resource.Error(result.message ?: "An error occurred while fetching game Detail data.")

            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }


    }


}

