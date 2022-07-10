package com.steinbock.discgolfgo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;

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

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        String val = pref.getString("map_style", "MAP_TYPE_HYBRID");
        val = val.substring(val.lastIndexOf('_') + 1);
        findPreference("map_style").setSummary("\t" + val);

        SeekBarPreference zoom = findPreference("zoom_level");
        zoom.setMin(1);
        zoom.setMax(100);
        zoom.setSeekBarIncrement(1);
        zoom.setShowSeekBarValue(true);
        zoom.setValue(pref.getInt("zoom_level", 90));

        pref.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> {
            if (s.equals("map_style")) {
                String val2 = sharedPreferences.getString(s, "MAP_TYPE_HYBRID");
                val2 = val2.substring(val2.lastIndexOf('_') + 1);
                System.out.println("Preference: " + s + ", " + val2);
                findPreference(s).setSummary("\t" + val2);
            }
        });
    }
}