package com.tayyipgunay.gamepedia.presentation.favorites.event

import com.tayyipgunay.gamepedia.domain.model.Game

sealed class FavoriteEvent {
    object getFavoritesGames : FavoriteEvent()//neden object

    // Favori oyun ekleme

    // Favori oyunu silme
    data class DeleteFavorite(val gameId: Int) : FavoriteEvent()
}