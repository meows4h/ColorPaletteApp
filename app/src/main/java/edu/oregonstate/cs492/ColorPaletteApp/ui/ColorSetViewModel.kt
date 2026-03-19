package edu.oregonstate.cs492.ColorPaletteApp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSet
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSetRepository
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorMindService
import kotlinx.coroutines.launch
import com.google.android.material.button.MaterialButton
import edu.oregonstate.cs492.ColorPaletteApp.R

class ColorSetViewModel: ViewModel() {
    private val repository = ColorSetRepository(ColorMindService.create())

    private val _colorSet = MutableLiveData<ColorSet?>(null)
    val colorSet: LiveData<ColorSet?> = _colorSet

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _locks = MutableLiveData<List<Boolean>>()
    val locks: LiveData<List<Boolean>> = _locks

    fun loadColorPalette(input: List<String>, model: String?) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadColorPalette(input, model)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _colorSet.value = result.getOrNull()
            _locks.value = repository.getLocks()
        }
    }

    fun toggleLock(idx: Int, state: Boolean, button: MaterialButton) {
        repository.setLock(idx, state)
        _locks.value = repository.getLocks()
        // honestly. this is a really bad solution
        // but it works!
        button.setOnClickListener {
            toggleLock(idx, !state, button)
            val tempIcon = if (!state) {
                R.drawable.ic_locked
            } else {
                R.drawable.ic_unlocked
            }
            button.setIconResource(tempIcon)
        }
    }

}