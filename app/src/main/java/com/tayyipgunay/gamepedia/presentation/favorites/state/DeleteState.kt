package com.tayyipgunay.gamepedia.presentation.favorites.state

data class DeleteGameState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
