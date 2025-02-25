package com.tayyipgunay.gamepedia.presentation.game_details.event

sealed class GameDetailsEvent{
    data class getGameDetails(val gameId: Int) : GameDetailsEvent()
    data class getGameScreenshots(val gameSlug: String) : GameDetailsEvent()
    data class getGameTrailers(val gameId: Int) : GameDetailsEvent()


}