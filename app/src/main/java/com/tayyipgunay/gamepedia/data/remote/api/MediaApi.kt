package com.tayyipgunay.gamepedia.data.remote.api

import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApi {
    @GET("games/{id}/movies")
    suspend fun getGameTrailers(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = Constants.API_KEY
    ): Response<GameTrailerResponseDto>
}