package com.tayyipgunay.gamepedia.domain.usecase.favorites

import com.tayyipgunay.gamepedia.data.local.gameEntitytoDomain
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class getFavoritesGameFromRoomUseCase @Inject constructor(private val localGameRepository: LocalGameRepository) {

    suspend fun executegetFavoritesGameFromRoomUseCase(): Resource<List<Game>> {
        val result = localGameRepository.getAllGames()


        when (result) {

            is Resource.Error -> {
                println("veri çekme  Resource error içindeyiz " + result.message)

                return Resource.Error(
                    result.message ?: "An error occurred while fetching game data."
                )


            }

            is Resource.Loading -> {
                println("veri çekme  Resource loading içindeyiz " + result.message)

                return Resource.Loading()

            }

            is Resource.Success -> {
                println("veri çekme  Resource success içindeyiz " + result.message)
                println("boşmu var mı  yukarıda yazıyor")
                val resulData=result.data!!.map {
                    it.gameEntitytoDomain()

                }
                return Resource.Success(resulData)

                }



            }
        }
    }





