package edu.oregonstate.cs492.ColorPaletteApp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ColorSetRepository (
    private val service: ColorMindService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var tempPalette: MutableList<String>? = null
    private var storedPalette: ColorSet? = null
    private var storedLocks: MutableList<Boolean>? = null

    fun setLock(idx: Int, lock: Boolean) {
        storedLocks?.let {
            if (idx in it.indices) {
                it[idx] = lock
            }
        }
    }

    fun getLocks() : List<Boolean>? {
        return storedLocks
    }

    fun getPalette() : ColorSet? {
        tempPalette?.let {
            storedPalette = ColorSet(it)
        }
        return storedPalette
    }

    fun updateColor(idx: Int, color: String) {
        tempPalette?.let {
            if (idx in it.indices) {
                it[idx] = color
                storedPalette = ColorSet(it)
            }
        }
    }

    fun setNewPalette(colors: List<String>) {
        tempPalette = colors.toMutableList()
        storedPalette = ColorSet(tempPalette!!)
        storedLocks = MutableList(colors.size) { false }
    }

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

                // if a value is not locked, we pass in N for a new color
                val lockCheckInput = processedInput.toMutableList()
                val locks = storedLocks ?: listOf()

                // this all could probably be cleaner but this works i guess
                for (idx in locks.indices) {
                    if (idx < lockCheckInput.size && !locks[idx]) {
                        lockCheckInput[idx] = "N"
                    }
                }

                val response = service.loadColorPalette(ColorMindRequest(lockCheckInput, model))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (storedLocks == null) {
                            storedPalette = body
                            storedLocks = MutableList(body.colors.size) { false }
                            tempPalette = body.colors.toMutableList()
                        } else {
                            for (idx in storedLocks!!.indices) {
                                if (!storedLocks!![idx] && idx < body.colors.size) {
                                    tempPalette!![idx] = body.colors[idx]
                                }
                            }
                        }
                        storedPalette = ColorSet(tempPalette!!)
                        Result.success(storedPalette)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                } else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
