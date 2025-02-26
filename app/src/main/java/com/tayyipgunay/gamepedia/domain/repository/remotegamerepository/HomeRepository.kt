package com.tayyipgunay.gamepedia.domain.repository.remotegamerepository

import com.tayyipgunay.gamepedia.data.dto.GameDto
import com.tayyipgunay.gamepedia.util.Resource

interface HomeRepository {

    // Belirtilen sıralama kriterine, sayfa boyutuna ve sayfa numarasına göre oyunları getirir
    suspend fun getGames(ordering: String, pageSize: Int, page: Int): Resource<GameDto>

    // Kullanıcının girdiği arama sorgusuna göre oyunları getirir
    suspend fun searchGames(query: String, page: Int): Resource<GameDto>

    // Belirtilen türe (genre), sayfa boyutuna ve sayfa numarasına göre oyunları getirir
    suspend fun getGenry(genres: String, pageSize: Int, page: Int): Resource<GameDto>

    // Ana sayfada gösterilecek banner oyunlarını getirir
    suspend fun getBannerGames(): Resource<GameDto>
}
