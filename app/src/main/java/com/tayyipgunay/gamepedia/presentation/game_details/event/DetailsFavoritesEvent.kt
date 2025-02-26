package com.tayyipgunay.gamepedia.presentation.game_details.event

import com.tayyipgunay.gamepedia.domain.model.Game

// DetailsFavoritesEvent: Detay sayfasında favorilerle ilgili olayları yönetmek için kullanılan sealed class
sealed class DetailsFavoritesEvent {

    // Oyunu favorilere eklemek için kullanılan event
    data class AddToFavorites(val game: Game) : DetailsFavoritesEvent()

    // Oyunun favorilerde olup olmadığını kontrol etmek için kullanılan event
    data class CheckIfFavorited(val gameId: Int) : DetailsFavoritesEvent()
}
