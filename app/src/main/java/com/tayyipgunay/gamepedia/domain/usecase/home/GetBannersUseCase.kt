package com.tayyipgunay.gamepedia.domain.usecase.home

import com.tayyipgunay.gamepedia.data.dto.toBannerDomainModel

import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject


class GetBannersUseCase @Inject constructor(private val homeRepository: HomeRepository) {


    suspend fun BannerExecute(): Resource<List<Banner>>{
        val result=homeRepository.getBannerGames()
      return when(result){
          is Resource.Success->{
              Resource.Success(result.data!!.toBannerDomainModel())
          }

          is Resource.Error -> {
              Resource.Error(result.message!!)
          }
          is Resource.Loading -> {
             Resource.Loading()
          }
      }



    }

}


