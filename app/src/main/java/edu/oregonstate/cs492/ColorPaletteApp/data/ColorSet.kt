package edu.oregonstate.cs492.ColorPaletteApp.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

data class ColorSet (
    val colors: List<String>
)

@JsonClass(generateAdapter = true)
data class ColorMindResultJson(
    val result: List<List<Int>>
)

class ColorMindColorJsonAdapter {
    @FromJson
    fun colorFromJson(json: ColorMindResultJson): ColorSet {
        val hexColors = json.result.map { rgb ->
            val hex = rgb.joinToString("") { value ->
                value.toString(16).padStart(2, '0').uppercase()
            }
            "#$hex"
        }

        return ColorSet(colors = hexColors)
    }

    @ToJson
    fun colorToJson(colorSet: ColorSet): String {
        throw UnsupportedOperationException("encoding colorSet to JSON is not supported")
    }
}