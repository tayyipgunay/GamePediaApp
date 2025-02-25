package com.tayyipgunay.gamepedia.domain.usecase.home


import com.tayyipgunay.gamepedia.data.dto.toGameDomainModel
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject


class SearchGamesUseCase @Inject constructor (private val homeRepository: HomeRepository) {

    /*suspend fun ExecuteSearchGames(query: String): List<Game> {
        val response = gameRepository.searchGames(query)
        if (response.isSuccessful) {
            return response.body()?.toGameDomainModel()?: emptyList()
        } else {
            throw Exception("API Error: ${response.code()} - ${response.message()}")
        }
        }
        }*/


    suspend fun executeSearchGames(query: String,page:Int): Resource<List<Game>> {
        val result = homeRepository.searchGames(query,page)
        return when (result) {
            is Resource.Success -> {
                // Repository zaten boş sonuç kontrolünü yaptıysa, sadece dönüşüm yapabilirsiniz
                Resource.Success(result.data!!.toGameDomainModel())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "An error occurred while searching for games.")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }

    /*suspend fun executeSearchGames(query: String): Resource<List<Game>> {
        // Repository'den veri al
        val result = gameRepository.searchGames(query)

        // UseCase'de iş mantığını işle
        return when (result) {
            is Resource.Success -> {
                val gameDto = result.data
                if (gameDto?.results.isNullOrEmpty()) {
                    Resource.Error("No games found your Search")
                } else {
                    // Domain modeline dönüştür ve başarı ile döndür
                    Resource.Success(gameDto!!.toGameDomainModel())
                }
            }
            is Resource.Error -> {
                // Hata durumunu ilet
                println("UseCase error message: ${result.message}")
                Resource.Error(result.message ?: "An error occurred while fetching game data.")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }*/

}

