package com.tayyipgunay.gamepedia.domain.repository.remotegamerepository

import com.tayyipgunay.gamepedia.data.dto.GameDto
import com.tayyipgunay.gamepedia.util.Resource

interface HomeRepository {
    suspend fun getGames(ordering: String,pageSize:Int,page:Int): Resource<GameDto>

    suspend fun searchGames(query: String,page:Int): Resource<GameDto>


    suspend fun getGenry(genres: String,pageSize: Int,page: Int): Resource<GameDto>



    suspend fun getBannerGames(): Resource<GameDto>
}