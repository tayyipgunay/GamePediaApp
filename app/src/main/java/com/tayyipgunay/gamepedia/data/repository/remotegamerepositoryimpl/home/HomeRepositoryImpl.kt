package com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.home

import com.tayyipgunay.gamepedia.data.dto.GameDto
import com.tayyipgunay.gamepedia.data.remote.api.HomeApi
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {

    // Oyunları belirli bir sıralamaya, sayfa boyutuna ve sayfa numarasına göre getirir
    override suspend fun getGames(ordering: String, pageSize: Int, page: Int): Resource<GameDto> {
        return try {
            val response = homeApi.getGames(ordering = ordering, pageSize = pageSize, page = page)

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let { body ->
                    if (body.results.isEmpty()) {
                        Resource.Error("No games found.") // Oyun listesi boşsa hata döndür
                    } else {
                        Resource.Success(body) // Başarıyla oyunları döndür
                    }
                } ?: Resource.Error("No games found.") // Null kontrolü
            } else {
                Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası döndür
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

    // Kullanıcının girdiği sorguya göre oyun arama işlemi yapar
    override suspend fun searchGames(query: String, page: Int): Resource<GameDto> {
        return try {
            val response = homeApi.searchGames(query = query, page = page)

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let { body ->
                    if (body.results.isNullOrEmpty()) {
                        Resource.Error("No games found.") // Eğer arama sonucu boşsa hata döndür
                    } else {
                        Resource.Success(body) // Başarıyla sonucu döndür
                    }
                } ?: Resource.Error("No games found.")
            } else {
                println("Response başarılı değil")
                println("API Error: ${response.message()}")
                Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası yönetimi
            }
        } catch (e: IOException) {
            println("İnternet bağlantısı yok")
            delay(1000) // 1 saniye gecikme ekleyerek tekrar deneme yapılabilir
            Resource.Error("İnternet bağlantısı yok")
        } catch (e: Exception) {
            println("Beklenmeyen bir hata oluştu: ${e.message}")
            e.printStackTrace() // Daha iyi hata ayıklama için log ekle
            Resource.Error(e.message ?: "Beklenmeyen bir hata oluştu") // Genel hata yönetimi
        }
    }

    // Belirtilen türe (genre) göre oyunları getirir
    override suspend fun getGenry(genres: String, pageSize: Int, page: Int): Resource<GameDto> {
        return try {
            val response = homeApi.getGenry(genres = genres, pageSize = pageSize, page = page)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    if (body.results.isEmpty()) {
                        return Resource.Error("No games found.") // Eğer sonuç boşsa hata döndür
                    } else {
                        return Resource.Success(body) // Başarıyla oyunları döndür
                    }
                } ?: return Resource.Error("No games found.") // Null kontrolü
            } else {
                return Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası yönetimi
            }
        } catch (e: IOException) {
            println("İnternet bağlantısı yok")
            delay(1000) // 1 saniye gecikme ekleyerek tekrar deneme yapılabilir
            Resource.Error("İnternet bağlantısı yok")
        } catch (e: Exception) {
            println("Beklenmeyen bir hata oluştu")
            Resource.Error(e.message ?: "Beklenmeyen bir hata oluştu") // Genel hata yönetimi
        }
    }

    // Banner oyunlarını getirir (özel öne çıkan oyunlar)
    override suspend fun getBannerGames(): Resource<GameDto> {
        return try {
            val response = homeApi.getBanners()

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.results.isNullOrEmpty()) {
                    Resource.Error("No games found.") // Eğer sonuç boşsa hata döndür
                } else {
                    Resource.Success(responseBody!!) // Başarıyla banner oyunlarını döndür
                }
            } else {
                Resource.Error("API Error: ${response.code()} - ${response.message()}") // API hatası yönetimi
            }
        } catch (e: IOException) {
            println("İnternet bağlantısı yok")
            delay(1000) // 1 saniye gecikme ekleyerek tekrar deneme yapılabilir
            Resource.Error("İnternet bağlantısı yok")
        } catch (e: Exception) {
            println("Beklenmeyen bir hata oluştu")
            Resource.Error(e.message ?: "Beklenmeyen bir hata oluştu") // Genel hata yönetimi
        }
    }
}
