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
    override suspend fun getGameDetails(gameId: Int): Resource<GameDetailsDTO> {
        return try {
            val response = detailsApi.getGameDetails(gameId = gameId)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    //response.body() bir DTO (tekil nesne) olduğundan, isEmpty() kullanılamaz.
                    //Bu durumda, null kontrolü yapmanız yeterlidir.
                    return Resource.Success(body)
                }
                    ?: return Resource.Error("No games found.")
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




    override suspend fun getScreenshoots(gameSlug: String): Resource<ScreenShootsDto> {
        return try {
            val response = detailsApi.getScreenshoots(gameSlugOrId = gameSlug)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    if (body.results.isEmpty()) {
                        return Resource.Error("No games found.")
                    } else {
                        return Resource.Success(body)
                    }
                } ?: return Resource.Error("No games found.")
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