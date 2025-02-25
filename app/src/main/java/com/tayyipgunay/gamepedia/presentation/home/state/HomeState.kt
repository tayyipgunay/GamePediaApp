package com.tayyipgunay.gamepedia.presentation.home.state

import com.tayyipgunay.gamepedia.domain.model.Game

data class HomeState(
    val games: List<Game>? = null,
    val isLoading: Boolean = false,
    val isSearchLoading: Boolean = false,
    val errorMessage: String? = null
)
