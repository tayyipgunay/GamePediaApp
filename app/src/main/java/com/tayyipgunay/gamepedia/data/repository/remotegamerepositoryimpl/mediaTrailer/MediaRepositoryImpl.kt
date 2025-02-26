package com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.mediaTrailer

import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.data.remote.api.MediaApi
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.VideoPlayerRepository
import com.tayyipgunay.gamepedia.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(private val mediaApi: MediaApi) : VideoPlayerRepository {

    // Belirtilen oyun ID'sine göre oyunun fragmanlarını (trailer) getirir
    override suspend fun getGameTrailerVideo(gameId: Int): Resource<GameTrailerResponseDto> {
        return try {
            val response = mediaApi.getGameTrailers(gameId = gameId)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    if (body.results.isEmpty()) {
                        return Resource.Error("No game trailers found.") // Eğer sonuç boşsa hata döndür
                    } else {
                        return Resource.Success(body) // Fragmanları başarıyla döndür
                    }
                } ?: return Resource.Error("No game trailers found.") // Null kontrolü
            } else {
                return Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası yönetimi
            }

        } catch (e: IOException) {
            println("İnternet bağlantısı yok")
            delay(1000) // 1 saniye gecikme ekleyerek tekrar deneme yapılabilir
            Resource.Error("İnternet bağlantısı yok") // İnternet bağlantısı hatası
        } catch (e: Exception) {
            println("Beklenmeyen bir hata oluştu")
            Resource.Error(e.message ?: "Beklenmeyen bir hata oluştu") // Genel hata yönetimi
        }
    }
}
