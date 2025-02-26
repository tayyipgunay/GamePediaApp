package com.tayyipgunay.gamepedia.domain.repository.remotegamerepository

import com.tayyipgunay.gamepedia.data.dto.GameDetailsDTO
import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.data.dto.ScreenShootsDto
import com.tayyipgunay.gamepedia.util.Resource

interface DetailsRepository {

    // Belirtilen oyun ID'sine göre oyun detaylarını getirir ve sonucu Resource olarak döndürür
    suspend fun getGameDetails(gameId: Int): Resource<GameDetailsDTO>

    // Belirtilen oyun slug'ına göre oyunun ekran görüntülerini getirir ve sonucu Resource olarak döndürür
    suspend fun getScreenshoots(gameSlug: String): Resource<ScreenShootsDto>
}
