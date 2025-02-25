package com.tayyipgunay.gamepedia.data.dto

import com.google.gson.annotations.SerializedName
import com.tayyipgunay.gamepedia.domain.model.Screenshot


data class ScreenShootsDto(
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<Screenshoots>
)


data class Screenshoots(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String
)

  fun ScreenShootsDto.toScreenshotDomainModel(): List<Screenshot> {
      return results.map { Screenshoots ->
          Screenshot(
              imageUrl = Screenshoots.image
          )
      }


  }


