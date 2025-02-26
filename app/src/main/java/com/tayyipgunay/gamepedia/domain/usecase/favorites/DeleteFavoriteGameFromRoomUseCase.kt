package com.tayyipgunay.gamepedia.domain.usecase.favorites

import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class deleteFavoriteGameFromRoomUseCase @Inject constructor(private val localGameRepository: LocalGameRepository) {

    suspend fun executeDeleteFavoriteGame(gameId: Int): Resource<Unit> {
        // Belirtilen gameId'ye sahip oyunu favorilerden siler
        val result = localGameRepository.deleteGameById(gameId)

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Error -> {
                // Hata durumunda, hata mesajını ekleyerek geri döndürür
                Resource.Error(
                    result.message ?: "An error occurred while deleting the game from favorites."
                )
            }

            is Resource.Loading -> {
                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }

            is Resource.Success -> {
                // Başarı durumunda, işlem başarılı olarak Unit döndürülür
                Resource.Success(Unit)
            }
        }
    }
}
