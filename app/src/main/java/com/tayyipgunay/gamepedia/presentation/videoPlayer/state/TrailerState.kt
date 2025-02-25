package com.tayyipgunay.gamepedia.presentation.videoPlayer.state

import com.tayyipgunay.gamepedia.domain.model.GameTrailer

data class TrailerState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val gameTrailer: List<GameTrailer>? = null
)
