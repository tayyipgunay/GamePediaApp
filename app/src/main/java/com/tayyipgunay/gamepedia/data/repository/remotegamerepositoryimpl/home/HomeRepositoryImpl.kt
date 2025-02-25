package com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.home

import com.tayyipgunay.gamepedia.data.dto.GameDto
import com.tayyipgunay.gamepedia.data.remote.api.HomeApi
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi): HomeRepository {
    override suspend fun getGames(ordering: String, pageSize: Int, page: Int): Resource<GameDto> {
        return try {
            val response = homeApi.getGames(ordering = ordering, pageSize = pageSize, page = page)

            if (response.isSuccessful) {
                val responseBody = response.body()


                responseBody?.let { body ->
                    if (body.results.isEmpty()) {
                        Resource.Error("No games found.")
                    } else {
                        Resource.Success(body)
                    }
                }?: Resource.Error("No games found.")

            }
            else {
                Resource.Error("API Error: ${response.code()} - ${response.message()}")

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
    override suspend fun searchGames(query: String,page:Int): Resource<GameDto> {
        return try {
            val response = homeApi.searchGames(query = query, page = page)


            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let { body ->
                    if (body.results.isNullOrEmpty()) {
                        Resource.Error("No games found.")
                    } else {
                        Resource.Success(body)
                    }
                } ?: Resource.Error("No games found.")
            } else {
                println("response başarılı değill")

                println("API Error: ${response.message()}")
                Resource.Error("API Error: ${response.code()} - ${response.message()}")
            }
        }
        catch (e: IOException) {
            println("No internet connection.")
            delay(1000) // 1-second delay, consider retry logic here
            Resource.Error("No internet connection.")
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
            e.printStackTrace() // Add this for better debugging
            Resource.Error(e.message ?: "An unexpected error occurred.")
        }
    }




    override suspend fun getGenry(genres: String, pageSize: Int, page: Int): Resource<GameDto> {
        return try {
            val response = homeApi.getGenry(genres = genres, pageSize = pageSize, page = page)

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




    override suspend fun getBannerGames(): Resource<GameDto> {
        return try {
            val response = homeApi.getBanners()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.results.isNullOrEmpty()) {
                    Resource.Error("No games found.")
                } else {
                    Resource.Success(responseBody!!)
                }
            } else {
                Resource.Error("API Error: ${response.code()} - ${response.message()}")
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