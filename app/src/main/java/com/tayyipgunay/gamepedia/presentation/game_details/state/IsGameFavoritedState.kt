package com.tayyipgunay.gamepedia.presentation.game_details.state

data class isGameFavoritedState(
    val isLoading: Boolean = false,
    val success:Boolean=false,
    val errorMessage: String? = null
)