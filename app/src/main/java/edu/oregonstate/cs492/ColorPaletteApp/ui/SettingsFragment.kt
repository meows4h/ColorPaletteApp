package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import edu.oregonstate.cs492.ColorPaletteApp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}