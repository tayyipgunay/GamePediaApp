package com.tayyipgunay.gamepedia.domain.usecase.home

import com.tayyipgunay.gamepedia.data.dto.toGameDomainModel
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class getGenresUseCase @Inject constructor(private val homeRepository: HomeRepository) {


    suspend fun executeGenres(genres: String,pageSize: Int,page:Int): Resource<List<Game>> {
         val result=homeRepository.getGenry(genres,pageSize,page)
         return when (result) {
             is Resource.Success -> {
                 Resource.Success(result.data!!.toGameDomainModel())
             }

             is Resource.Error -> {
                 Resource.Error(result.message?:"Film verisi alınırken bir hata oluştu.")

             }


             is Resource.Loading->Resource.Loading()
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


