package edu.oregonstate.cs492.ColorPaletteApp.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ColorMindModel (
    @Json(name = "result") val models: List<String>
)
