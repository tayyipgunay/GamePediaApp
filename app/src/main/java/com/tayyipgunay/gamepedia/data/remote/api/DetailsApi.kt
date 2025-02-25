package com.tayyipgunay.gamepedia.data.remote.api

import com.tayyipgunay.gamepedia.data.dto.GameDetailsDTO
import com.tayyipgunay.gamepedia.data.dto.ScreenShootsDto
import com.tayyipgunay.gamepedia.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsApi {
    // Oyun Detayları
    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = Constants.API_KEY
    ): Response<GameDetailsDTO>


    @GET("games/{game_pk}/screenshots")
    suspend fun getScreenshoots(
        @Path("game_pk") gameSlugOrId: String,
        @Query("key") apiKey: String= Constants.API_KEY
    ): Response<ScreenShootsDto>
}