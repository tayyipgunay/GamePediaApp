package com.tayyipgunay.gamepedia.domain.usecase.home


import com.tayyipgunay.gamepedia.data.dto.toGameDomainModel
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class GetGamesUseCase @Inject constructor (private val homeRepository: HomeRepository) {

    suspend fun GetGamesExecute(ordering: String, pageSize: Int, page: Int): Resource<List<Game>> {
        // Belirtilen sıralama kriterine, sayfa boyutuna ve sayfa numarasına göre oyunları getirir
        val result = homeRepository.getGames(ordering, pageSize, page)

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Success -> {
                // Başarı durumunda, DTO verisini domain modeline dönüştürerek döndürür
                Resource.Success(result.data!!.toGameDomainModel())
            }

            is Resource.Error -> {
                // Hata durumunda, hata mesajını ekleyerek geri döndürür
                Resource.Error(result.message ?: "An error occurred while fetching game data.")
            }

            is Resource.Loading -> {
                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }
        }
    }
}


