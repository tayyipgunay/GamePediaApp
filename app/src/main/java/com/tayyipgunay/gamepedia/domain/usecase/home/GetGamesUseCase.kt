package com.tayyipgunay.gamepedia.domain.usecase.home


import com.tayyipgunay.gamepedia.data.dto.toGameDomainModel
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class GetGamesUseCase @Inject constructor (private val homeRepository: HomeRepository) {

    suspend fun GetGamesExecute(ordering: String, pageSize: Int,page:Int): Resource<List<Game>> {
        val result = homeRepository.getGames(ordering, pageSize,page)
        return when (result) {
            is Resource.Success -> {
                Resource.Success(result.data!!.toGameDomainModel())
            }

            is Resource.Error -> {
                Resource.Error(result.message ?: "An error occurred while fetching game data.")
            }

            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }
}

