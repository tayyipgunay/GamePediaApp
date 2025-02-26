package com.tayyipgunay.gamepedia.domain.usecase.details

import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class isGameFavoritedUseCase @Inject constructor(private val localGameRepository: LocalGameRepository) {


    suspend fun execute(gameId: Int): Resource<Boolean> {
        // Belirtilen gameId'ye sahip oyunun favorilere eklenip eklenmediğini kontrol eder
        val result = localGameRepository.isGameFavorited(gameId)

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Success -> {
                // Başarı durumunda, sonucu Boolean olarak döndürür
                Resource.Success(result.data!!)
            }

            is Resource.Error -> {
                // Hata durumunda, hata mesajını ekleyerek geri döndürür
                Resource.Error(
                    result.message ?: "An error occurred while checking if the game is favorited."
                )
            }

            is Resource.Loading -> {
                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }
        }
    }
}
