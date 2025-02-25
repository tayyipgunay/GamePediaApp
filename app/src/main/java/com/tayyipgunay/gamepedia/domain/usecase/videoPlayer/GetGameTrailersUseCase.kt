package com.tayyipgunay.gamepedia.domain.usecase.videoPlayer

import com.tayyipgunay.gamepedia.data.dto.toGameTrailers
import com.tayyipgunay.gamepedia.domain.model.GameTrailer
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.VideoPlayerRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class GetGameTrailersUseCase @Inject constructor(private val videoPlayerRepository: VideoPlayerRepository) {
    /* suspend fun executeGetGameTrailers(gameId: Int): List<GameTrailer> {
        val response = gameRepository.getGameTrailerVideo(gameId)
        if (response.isSuccessful) {
            return response.body()?.toGameTrailers()?: emptyList()
        } else {
            throw Exception("API Error: ${response.code()} - ${response.message()}")
        }
    }*/


    suspend fun executeGetGameTrailers(gameId: Int): Resource<List<GameTrailer>> {
        val response = videoPlayerRepository.getGameTrailerVideo(gameId)
        return when (response) {
            is Resource.Success -> {
                Resource.Success(response.data!!.toGameTrailers())

            }

            is Resource.Error -> {
                Resource.Error(response.message!!)
            }

            is Resource.Loading -> {
                Resource.Loading()
            }
        }

    }
}


