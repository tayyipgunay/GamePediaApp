package com.tayyipgunay.gamepedia.presentation.home.state

import com.tayyipgunay.gamepedia.domain.model.Banner

data class TopRatedGamesState(
    val isLoading: Boolean = false,
    val bannerImages: List<Banner>? = null,
    val error: String? = null
)
