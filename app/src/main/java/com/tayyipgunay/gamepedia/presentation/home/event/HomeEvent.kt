package com.tayyipgunay.gamepedia.presentation.home.event

sealed class HomeEvent {
    data class getGames(val ordering: String, val pageSize: Int, val page: Int) : HomeEvent()
    data class getGenres (val genres: String, val pageSize: Int, val page: Int) : HomeEvent()
    data class searchGames(val query: String, val page: Int) : HomeEvent()
    object getBanner : HomeEvent() // Parametresiz bir event için object kullanılır



}







