package com.tayyipgunay.gamepedia.presentation.game_details.event

import com.tayyipgunay.gamepedia.domain.model.Game

sealed class DetailsFavoritesEvent {
    data class AddToFavorites(val game: Game) : DetailsFavoritesEvent()

    // Oyunun favorilerde olup olmadığını kontrol etme
    data class CheckIfFavorited(val gameId: Int) : DetailsFavoritesEvent()
}