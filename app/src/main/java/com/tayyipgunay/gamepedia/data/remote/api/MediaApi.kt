package com.tayyipgunay.gamepedia.data.remote.api

import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApi {

    // Belirtilen oyun ID'sine göre oyunun fragmanlarını (trailers) getirir
    @GET("games/{id}/movies")
    suspend fun getGameTrailers(
        @Path("id") gameId: Int, // API isteğinde oyun ID'sini belirler
        @Query("key") apiKey: String = Constants.API_KEY // API anahtarını parametre olarak ekler
    ): Response<GameTrailerResponseDto> // API'den dönen yanıtı GameTrailerResponseDto olarak işler
}
