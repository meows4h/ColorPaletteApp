package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.ColorPaletteApp.data.AppDatabase
import edu.oregonstate.cs492.ColorPaletteApp.data.Palette
import edu.oregonstate.cs492.ColorPaletteApp.data.PaletteRepository
import kotlinx.coroutines.launch

class PaletteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PaletteRepository(AppDatabase.Companion.getInstance(application).paletteDao())
    val palettes = repository.getAll().asLiveData()

    fun addPalette(palette: Palette) {
        viewModelScope.launch {
            repository.insert(palette)
        }
    }

    fun deletePalette(palette: Palette) {
        viewModelScope.launch {
            repository.delete(palette)
        }
    }
}