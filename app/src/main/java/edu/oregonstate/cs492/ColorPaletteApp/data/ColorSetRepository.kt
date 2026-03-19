package edu.oregonstate.cs492.ColorPaletteApp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ColorSetRepository (
    private val service: ColorMindService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var storedPalette: ColorSet? = null

    suspend fun loadColorPalette(
        input: List<String>,
        model: String?
    ) : Result<ColorSet?> {
        return withContext(ioDispatcher) {
            try {

                // we are processing either to pass "N" for a new color
                // or a hexcode to parse back into listOf(R,G,B)
                val processedInput: List<Any> = input.map { valOrN ->
                    if (valOrN == "N") {
                        "N"
                    } else {
                        val cleanHex = valOrN.removePrefix("#")
                        if (cleanHex.length == 6) {
                            try {
                                listOf(
                                    cleanHex.substring(0, 2).toInt(16),
                                    cleanHex.substring(2, 4).toInt(16),
                                    cleanHex.substring(4, 6).toInt(16)
                                )
                            } catch (e: Exception) {
                                "N"
                            }
                        } else {
                            "N"
                        }
                    }
                }

                val response = service.loadColorPalette(ColorMindRequest(processedInput, model))
                if (response.isSuccessful) {
                    storedPalette = response.body()
                    Result.success(storedPalette)
                } else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}