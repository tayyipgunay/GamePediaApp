package com.tayyipgunay.gamepedia.domain.usecase.home

import com.tayyipgunay.gamepedia.data.dto.toGameDomainModel
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class getGenresUseCase @Inject constructor(private val homeRepository: HomeRepository) {


    suspend fun executeGenres(genres: String, pageSize: Int, page: Int): Resource<List<Game>> {
        // Belirtilen türe (genre), sayfa boyutuna ve sayfa numarasına göre oyunları getirir
        val result = homeRepository.getGenry(genres, pageSize, page)

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Success -> {
                // Başarı durumunda, DTO verisini domain modeline dönüştürerek döndürür
                Resource.Success(result.data!!.toGameDomainModel())
            }

            is Resource.Error -> {
                // Hata durumunda, hata mesajını ekleyerek geri döndürür
                Resource.Error(result.message ?: "Oyun verisi alınırken bir hata oluştu.")
            }

            is Resource.Loading -> {
                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }
        }
    }
}

/* suspend fun executeGenres(genres: String,pageSize: Int): List<Game> {
      val response= gameRepository.getGenry(genres,pageSize)

      if (response.isSuccessful) {
       return response.body()?.toGameDomainModel()?: emptyList()

      } else {
          throw Exception("API Error: ${response.code()} - ${response.message()}")

      }



  }*/


