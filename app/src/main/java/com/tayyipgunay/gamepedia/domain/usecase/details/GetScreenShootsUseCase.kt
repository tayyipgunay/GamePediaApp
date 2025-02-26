package com.tayyipgunay.gamepedia.domain.usecase.details

import com.tayyipgunay.gamepedia.data.dto.toScreenshotDomainModel
import com.tayyipgunay.gamepedia.domain.model.Screenshot
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.DetailsRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class GetScreenShootsUseCase @Inject constructor(private val detailsRepository: DetailsRepository) {

    /* suspend fun executeGetScreenShoots(gameSlugOrId: String):List<Screenshot>{
        val response=gameRepository.getScreenshoots(gameSlugOrId)
        if (response.isSuccessful){
            return response.body()?.toScreenshotDomainModel()?: emptyList()
        }else{
            throw Exception("API Error: ${response.code()} - ${response.message()}")


        }


    }*/

    suspend fun executeGetScreenShoots(gameSlugOrId: String): Resource<List<Screenshot>> {
        // detailsRepository üzerinden oyunun ekran görüntülerini getirir
        val result = detailsRepository.getScreenshoots(gameSlugOrId)

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Success -> {
                // Başarı durumunda, DTO verisini domain modeline dönüştürerek döndürür
                Resource.Success(result.data!!.toScreenshotDomainModel())
            }

            is Resource.Error -> {
                // Hata durumunda, hata mesajını ekleyerek geri döndürür
                Resource.Error(result.message!!)
            }

            is Resource.Loading -> {
                // Yüklenme (loading) durumu devam ederken bu durumu geri döndürür
                Resource.Loading()
            }
        }
    }
}
