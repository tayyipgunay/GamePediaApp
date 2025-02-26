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
        // Belirtilen oyun ID'sine göre fragmanları (trailers) getirir
        val response = videoPlayerRepository.getGameTrailerVideo(gameId)

        // Resource türüne göre işlem yapılır
        return when (response) {
            is Resource.Success -> {
                // Başarı durumunda, DTO verisini domain modeline dönüştürerek döndürür
                Resource.Success(response.data!!.toGameTrailers())
            }

            is Resource.Error -> {
                // Hata durumunda, hata mesajını ekleyerek geri döndürür
                Resource.Error(response.message!!)
            }

            is Resource.Loading -> {
                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }
        }
    }
}


