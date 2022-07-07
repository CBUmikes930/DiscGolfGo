package com.steinbock.discgolfgo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        findPreference("map_style").setSummary("\t" + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("map_style", "MAP_TYPE_HYBRID"));

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> {
            if (s.equals("map_style")) {
                System.out.println("Preference: " + s + ", " + sharedPreferences.getString(s, "N/A"));
                findPreference(s).setSummary("\t" + sharedPreferences.getString(s, "N/A"));
            }
        });
    }
}