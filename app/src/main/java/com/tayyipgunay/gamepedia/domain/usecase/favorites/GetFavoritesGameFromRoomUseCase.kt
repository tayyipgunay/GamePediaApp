package com.tayyipgunay.gamepedia.domain.usecase.favorites

import com.tayyipgunay.gamepedia.data.local.gameEntitytoDomain
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class getFavoritesGameFromRoomUseCase @Inject constructor(private val localGameRepository: LocalGameRepository) {

    suspend fun executegetFavoritesGameFromRoomUseCase(): Resource<List<Game>> {
        // Tüm favori oyunları Room veritabanından getirir
        val result = localGameRepository.getAllGames()

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Error -> {
                println("Veri çekme işlemi sırasında hata oluştu: " + result.message)

                // Hata mesajı ile birlikte hata durumunu döndürür
                Resource.Error(
                    result.message ?: "An error occurred while fetching game data."
                )
            }

            is Resource.Loading -> {
                println("Veri çekme işlemi devam ediyor: " + result.message)

                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }

            is Resource.Success -> {
                println("Veri çekme işlemi başarılı: " + result.message)
                println("Oyun listesi boş mu? Sonuç yukarıda yazıyor.")

                // Veritabanından gelen GameEntity listesini Game domain modeline dönüştürür
                val resultData = result.data!!.map {
                    it.gameEntitytoDomain()
                }

                // Başarıyla dönüştürülmüş veriyi döndürür
                Resource.Success(resultData)
            }
        }
    }
}

