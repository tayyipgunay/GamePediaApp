package com.tayyipgunay.gamepedia.presentation.game_details.state

import com.tayyipgunay.gamepedia.domain.model.GameDetail

data class GameDetailsState(
    val isLoading: Boolean = false,          // Yükleme durumu
    val gameDetail:GameDetail?=null,     // Oyun detayları
    val error: String? = null, // Hata mesajı


)
