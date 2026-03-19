package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import edu.oregonstate.cs492.ColorPaletteApp.R
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorMindService
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val modelPreference = findPreference<ListPreference>(getString(R.string.pref_model_key))

        // creating a summary provider for the list preference
        modelPreference?.summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
            val value = preference.value
            if (value == null) {
                getString(R.string.pref_model_default)
            } else {
                val index = preference.findIndexOfValue(value)
                if (index >= 0 && preference.entries != null && index < preference.entries.size) {
                    preference.entries[index]
                } else {
                    // in case nothing loads properly
                    formatModelName(value)
                }
            }
        }

        // creating the list of models for the list preference
        // based on the GET request of the service
        lifecycleScope.launch {
            try {
                val service = ColorMindService.create()
                val response = service.loadModelList()
                if (response.isSuccessful) {
                    val models = response.body()?.models ?: listOf("default")
                    
                    val entries = models.map { formatModelName(it) }

                    modelPreference?.entries = entries.toTypedArray()
                    modelPreference?.entryValues = models.toTypedArray()
                    
                    if (modelPreference?.value == null) {
                        modelPreference?.value = "default"
                    }
                }
            } catch (e: Exception) {
                modelPreference?.entries = arrayOf("Default")
                modelPreference?.entryValues = arrayOf("default")
                if (modelPreference?.value == null) {
                    modelPreference?.value = "default"
                }
            }
        }
    }

    private fun formatModelName(name: String): String {
        return name.replace("_", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
    }
}