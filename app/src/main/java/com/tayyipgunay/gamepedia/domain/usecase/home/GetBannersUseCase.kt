package com.tayyipgunay.gamepedia.domain.usecase.home

import com.tayyipgunay.gamepedia.data.dto.toBannerDomainModel

import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject


class GetBannersUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun BannerExecute(): Resource<List<Banner>> {
        // Banner oyunlarını homeRepository üzerinden getirir
        val result = homeRepository.getBannerGames()

        // Resource türüne göre işlem yapılır
        return when (result) {
            is Resource.Success -> {
                // Başarı durumunda, DTO verisini domain modeline dönüştürerek döndürür
                Resource.Success(result.data!!.toBannerDomainModel())
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

