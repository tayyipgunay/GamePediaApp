package com.tayyipgunay.gamepedia.domain.model

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("background_image")
    val backgroundImage: String? // Banner görseli URL
)
