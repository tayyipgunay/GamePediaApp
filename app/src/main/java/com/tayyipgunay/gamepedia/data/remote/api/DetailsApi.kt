package com.tayyipgunay.gamepedia.data.remote.api

import com.tayyipgunay.gamepedia.data.dto.GameDetailsDTO
import com.tayyipgunay.gamepedia.data.dto.ScreenShootsDto
import com.tayyipgunay.gamepedia.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsApi {

    // Belirtilen oyun ID'sine göre oyun detaylarını getirir
    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int, // API isteğinde oyun ID'sini belirler
        @Query("key") apiKey: String = Constants.API_KEY // API anahtarını parametre olarak ekler
    ): Response<GameDetailsDTO> // API'den dönen yanıtı GameDetailsDTO olarak işler

    // Belirtilen oyun ID veya slug değerine göre oyunun ekran görüntülerini getirir
    @GET("games/{game_pk}/screenshots")
    suspend fun getScreenshoots(
        @Path("game_pk") gameSlugOrId: String, // API isteğinde oyun slug veya ID'sini belirler
        @Query("key") apiKey: String = Constants.API_KEY // API anahtarını parametre olarak ekler
    ): Response<ScreenShootsDto> // API'den dönen yanıtı ScreenShootsDto olarak işler
}
