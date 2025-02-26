package com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.details

import com.tayyipgunay.gamepedia.data.dto.GameDetailsDTO
import com.tayyipgunay.gamepedia.data.dto.ScreenShootsDto
import com.tayyipgunay.gamepedia.data.remote.api.DetailsApi
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.DetailsRepository
import com.tayyipgunay.gamepedia.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(private val detailsApi: DetailsApi) : DetailsRepository {

    // Belirtilen oyun ID'sine göre oyun detaylarını getirir ve sonucu Resource olarak döndürür
    override suspend fun getGameDetails(gameId: Int): Resource<GameDetailsDTO> {
        return try {
            val response = detailsApi.getGameDetails(gameId = gameId)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    // response.body() bir DTO (tekil nesne) olduğundan, isEmpty() kullanılamaz.
                    // Bu durumda, null kontrolü yapmak yeterlidir.
                    return Resource.Success(body)
                } ?: return Resource.Error("No games found.") // Eğer body null ise hata döndürülür
            } else {
                return Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası durumunda hata mesajı döndürülür
            }

        } catch (e: IOException) {
            println("İnternet bağlantısı yok")
            delay(1000) // 1 saniye gecikme ekleyerek tekrar deneme yapılabilir
            Resource.Error("İnternet bağlantısı yok") // İnternet bağlantısı hatası durumunda hata mesajı döndürülür
        } catch (e: Exception) {
            println("Beklenmeyen bir hata oluştu")
            Resource.Error(e.message ?: "Beklenmeyen bir hata oluştu") // Diğer tüm hatalar için genel hata mesajı döndürülür
        }
    }

    // Belirtilen oyun slug veya ID'ye göre oyunun ekran görüntülerini getirir ve sonucu Resource olarak döndürür
    override suspend fun getScreenshoots(gameSlug: String): Resource<ScreenShootsDto> {
        return try {
            val response = detailsApi.getScreenshoots(gameSlugOrId = gameSlug)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    if (body.results.isEmpty()) {
                        return Resource.Error("No games found.") // Eğer gelen veri boşsa hata döndürülür
                    } else {
                        return Resource.Success(body) // Eğer veri varsa başarıyla döndürülür
                    }
                } ?: return Resource.Error("No games found.") // Body null ise hata döndürülür
            } else {
                return Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası durumunda hata mesajı döndürülür
            }

        } catch (e: IOException) {
            println("İnternet bağlantısı yok")
            delay(1000) // 1 saniye gecikme
            Resource.Error("İnternet bağlantısı yok") // İnternet bağlantısı hatası durumunda hata mesajı döndürülür
        } catch (e: Exception) {
            println("Beklenmeyen bir hata oluştu")
            Resource.Error(e.message ?: "Beklenmeyen bir hata oluştu") // Diğer tüm hatalar için genel hata mesajı döndürülür
        }
    }
}
