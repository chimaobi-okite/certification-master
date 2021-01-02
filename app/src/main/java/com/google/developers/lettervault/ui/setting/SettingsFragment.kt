package com.google.developers.lettervault.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.developers.lettervault.R
import com.google.developers.lettervault.util.NightMode


class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nightPref = findPreference(getString(R.string.pref_key_night))

        nightPref.setOnPreferenceChangeListener { _, newValue ->
            val night = getString(R.string.pref_night_on)
            val day = getString(R.string.pref_night_off)
            val auto = getString(R.string.pref_night_auto)

            if (newValue is String) {
                when (newValue.toString()) {
                    auto -> updateTheme(AppCompatDelegate.MODE_NIGHT_AUTO)
                    day -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    night -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

}
