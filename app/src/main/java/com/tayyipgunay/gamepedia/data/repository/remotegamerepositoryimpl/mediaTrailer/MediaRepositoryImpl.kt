package com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.mediaTrailer

import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.data.remote.api.MediaApi
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.VideoPlayerRepository
import com.tayyipgunay.gamepedia.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(private val mediaApi: MediaApi) : VideoPlayerRepository {
    override suspend fun getGameTrailerVideo(gameId: Int): Resource<GameTrailerResponseDto> {
        return try {
            val response = mediaApi.getGameTrailers(gameId = gameId)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    if (body.results.isEmpty()) {
                        return Resource.Error("No games Trailer found.")
                    } else {
                        return Resource.Success(body)
                    }
                } ?: return Resource.Error("No games Trailer found.")
            } else {
                return Resource.Error("API Error: ${response.code()} - ${response.message()}")
            }

        } catch (e: IOException) {
            println("internet bağlantısı yok")
            delay(1000) // 1 saniye gecikme
            Resource.Error("İnternet bağlantısı yok:")
        } catch (e: Exception) {
            println("beklenmeyen bir hata oluştu")

            Resource.Error(e.message ?: "beklenmeyen bir hata oluştu")
        }
    }
}