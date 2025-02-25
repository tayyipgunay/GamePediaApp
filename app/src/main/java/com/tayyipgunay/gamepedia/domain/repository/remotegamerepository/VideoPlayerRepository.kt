package com.tayyipgunay.gamepedia.domain.repository.remotegamerepository

import com.tayyipgunay.gamepedia.data.dto.GameTrailerResponseDto
import com.tayyipgunay.gamepedia.util.Resource

interface VideoPlayerRepository {
    suspend fun getGameTrailerVideo(gameId: Int): Resource<GameTrailerResponseDto>

}