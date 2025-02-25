package com.tayyipgunay.gamepedia.presentation.favorites.state

import com.tayyipgunay.gamepedia.domain.model.Game

data class GetAllGamesState(
    val isLoading: Boolean = false,
    val games: List<Game>? = null,//GameEntity
    val errorMessage: String? = null
)
