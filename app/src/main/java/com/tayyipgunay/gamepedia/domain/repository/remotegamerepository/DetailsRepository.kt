package com.tayyipgunay.gamepedia.domain.repository.remotegamerepository

import com.tayyipgunay.gamepedia.data.dto.GameDetailsDTO
import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.data.dto.ScreenShootsDto
import com.tayyipgunay.gamepedia.util.Resource

interface DetailsRepository {
    suspend fun getGameDetails (gameId:Int): Resource<GameDetailsDTO>

    suspend fun getScreenshoots(gameSlug:String):Resource<ScreenShootsDto>



}