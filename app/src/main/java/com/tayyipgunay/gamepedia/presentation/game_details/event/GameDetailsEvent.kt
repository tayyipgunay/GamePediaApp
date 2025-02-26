package com.tayyipgunay.gamepedia.presentation.game_details.event

// GameDetailsEvent: Oyun detaylarıyla ilgili olayları yönetmek için kullanılan sealed class
sealed class GameDetailsEvent {

    // Belirtilen oyun ID'sine göre oyun detaylarını almak için kullanılan event
    data class getGameDetails(val gameId: Int) : GameDetailsEvent()

    // Belirtilen oyun slug'ına göre ekran görüntülerini almak için kullanılan event
    data class getGameScreenshots(val gameSlug: String) : GameDetailsEvent()

    // Belirtilen oyun ID'sine göre oyun fragmanlarını almak için kullanılan event
    data class getGameTrailers(val gameId: Int) : GameDetailsEvent()


}
