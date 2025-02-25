package com.tayyipgunay.gamepedia.data.remote.api

import com.tayyipgunay.gamepedia.data.dto.GameDetailsDTO
import com.tayyipgunay.gamepedia.data.dto.GameDto
import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.data.dto.ScreenShootsDto
import com.tayyipgunay.gamepedia.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Oyun Listesi (Öne çıkan veya sıralanmış oyunlar)
   /* @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String = Constants.API_KEY, // API Key
        @Query("ordering") ordering: String, //="-added",  // Default sıralama (en son eklenenler)-added
        @Query("page") page: Int,              // Sayfa numarası
        @Query("page_size") pageSize: Int          // oyun sayısı

    ): Response<GameDto>

    // Öne Çıkan Banner Oyunlar
    @GET("games")
    suspend fun getBanners(
        @Query("key") apiKey: String = Constants.API_KEY,
        @Query("ordering") ordering: String = "-rating", // En yüksek puanlı oyunlar
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10 // İlk 10 oyunu çek
    ): Response<GameDto>

    // Arama Yap
    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String = Constants.API_KEY,
        @Query("search") query: String,
        @Query("page") page: Int  ,// Sayfa numarası
        @Query("page_size") pageSize: Int = 40
    ): Response<GameDto>


    @GET("games")
    suspend fun getGenry(
        @Query("key") apiKey: String = Constants.API_KEY,
        @Query("genres") genres: String,
        @Query("page") page: Int ,// Sayfa numarası
        @Query("page_size") pageSize:Int
        ): Response<GameDto>


*/

    // Oyun Detayları
  /*  @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = Constants.API_KEY
    ): Response<GameDetailsDTO>


    @GET("games/{game_pk}/screenshots")
    suspend fun getScreenshoots(
        @Path("game_pk") gameSlugOrId: String,
        @Query("key") apiKey: String=Constants.API_KEY
    ): Response<ScreenShootsDto>
*/

//değiştirsek ne olur.






    // Fragman veya video listesini almak için yeni bir yöntem ekleyelim
   /* @GET("games/{id}/movies")
    suspend fun getGameTrailers(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = Constants.API_KEY
    ): Response<GameTrailerResponseDto>

*/






}