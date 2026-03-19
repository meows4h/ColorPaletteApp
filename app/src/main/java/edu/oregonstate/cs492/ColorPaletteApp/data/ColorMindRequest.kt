package edu.oregonstate.cs492.ColorPaletteApp.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ColorMindRequest(
    val input: List<Any>,
    val model: String? = "default"
)