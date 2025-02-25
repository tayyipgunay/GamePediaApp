package com.tayyipgunay.gamepedia.presentation.game_details.state

data class InsertState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false


    )
