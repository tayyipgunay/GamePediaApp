package com.tayyipgunay.gamepedia.presentation.game_details.state

import com.tayyipgunay.gamepedia.domain.model.Screenshot

data class ScreenShootState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val gameScreenshoots: List<Screenshot>? = null
)
