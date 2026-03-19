package edu.oregonstate.cs492.ColorPaletteApp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSet
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSetRepository
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorMindService
import kotlinx.coroutines.launch

class ColorSetViewModel: ViewModel() {
    private val repository = ColorSetRepository(ColorMindService.create())

    private val _colorSet = MutableLiveData<ColorSet?>(null)
    val colorSet: LiveData<ColorSet?> = _colorSet

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadColorPalette(input: List<String>, model: String?) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadColorPalette(input, model)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _colorSet.value = result.getOrNull()
        }
    }
}